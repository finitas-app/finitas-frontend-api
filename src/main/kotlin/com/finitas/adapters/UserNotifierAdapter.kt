package com.finitas.adapters

import com.finitas.config.Logger
import com.finitas.domain.ports.UserNotificationDto
import com.finitas.domain.ports.UserNotificationEvent
import com.finitas.domain.ports.UserNotifierPort
import com.finitas.domain.utils.getPetitioner
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
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

class UserNotifierAdapter : UserNotifierPort {
    private val logger by Logger()
    override suspend fun notifyUser(userNotificationDto: UserNotificationDto) {
        logger.info("Notify users: $userNotificationDto")
        userNotificationDto.targetUsers.forEach { (targetUsers, jsonData) ->
            targetUsers.forEach { idUser ->
                val connection = connections[idUser]
                connection?.values?.forEach {
                    it.send(
                        Json.encodeToString(
                            UserNotification(
                                userNotificationDto.event,
                                jsonData,
                            )
                        )
                    )
                }
            }
        }
    }
}


@Serializable
private data class UserNotification(
    val event: UserNotificationEvent,
    val jsonData: String?,
)