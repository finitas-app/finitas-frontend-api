package com.finitas.adapters

import com.finitas.config.Logger
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.ports.UserNotificationDto
import com.finitas.domain.ports.UserNotificationEvent
import com.finitas.domain.ports.UserNotifierPort
import com.finitas.domain.utils.getPetitioner
import io.github.crackthecodeabhi.kreds.connection.AbstractKredsSubscriber
import io.github.crackthecodeabhi.kreds.connection.Endpoint
import io.github.crackthecodeabhi.kreds.connection.KredsConnectionException
import io.github.crackthecodeabhi.kreds.connection.newClient
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import java.util.concurrent.ConcurrentHashMap


private val connections = ConcurrentHashMap<UUID, ConcurrentHashMap<UUID, DefaultWebSocketSession>>()

fun Route.userNotifier() {
    webSocket("/synchronizations") {
        val idUser = call.getPetitioner()
        val idConnection = UUID.randomUUID()
        connections.getOrPut(idUser) { ConcurrentHashMap() }[idConnection] = this

        incoming.consumeEach {}

        if (connections[idUser]?.size == 1) {
            connections.remove(idUser)
        } else {
            connections[idUser]?.remove(idConnection)
        }
    }
}

val userEventChannelName = "user_event"

class KredsSubscription(private val coroutineScope: CoroutineScope) : AbstractKredsSubscriber() {
    private val logger by Logger()

    override fun onException(ex: Throwable) {
        logger.error("Exception while handling subscription to redis.")
        ex.printStackTrace()
    }


    override fun onSubscribe(channel: String, subscribedChannels: Long) {
        logger.info("Subscribed to channel: $channel")
    }

    override fun onUnsubscribe(channel: String, subscribedChannels: Long) {
        logger.info("Unsubscribed from channel: $channel")
    }

    override fun onMessage(channel: String, message: String) {
        if (channel == userEventChannelName) {
            val userNotificationDto: UserNotificationDto = Json.decodeFromString(message)
            logger.info("Notify users: $userNotificationDto")
            userNotificationDto.targetUsers.forEach { (targetUsers, jsonData) ->
                targetUsers.forEach { idUser ->
                    val connection = connections[idUser]
                    connection?.values?.forEach {
                        coroutineScope.launch(Dispatchers.Default) {
                            try {

                                it.send(
                                    Json.encodeToString(
                                        UserNotification(
                                            userNotificationDto.event,
                                            jsonData,
                                        )
                                    )
                                )
                            }catch (e: Exception) {
                                logger.info("Failed to notify the user '$idUser'.")
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
    }
}

class UserNotifierAdapter(
    private val urlProvider: UrlProvider,
) : UserNotifierPort {
    private val logger by Logger()
    private val redisClient = newClient(Endpoint.from(urlProvider.REDIS_HOST_URL))

    override suspend fun notifyUser(userNotificationDto: UserNotificationDto) {
        logger.info("Push dto for notification: $userNotificationDto")
        try {

            redisClient.publish(userEventChannelName, Json.encodeToString(userNotificationDto))
        } catch (e: KredsConnectionException) {
            logger.error("Failed to notify users. Redis is not available.")
        }
    }
}

@Serializable
private data class UserNotification(
    val event: UserNotificationEvent,
    val jsonData: String?,
)