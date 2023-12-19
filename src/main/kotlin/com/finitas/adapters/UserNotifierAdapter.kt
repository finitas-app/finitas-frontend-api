package com.finitas.adapters

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


private val connections: MutableMap<UUID, DefaultWebSocketSession> = ConcurrentHashMap()

fun Route.userNotifier() {
    webSocket("/synchronizations") {
        val idUser = call.getPetitioner()
        connections[idUser] = this

        incoming.consumeEach {}

        connections.remove(idUser)
    }
}

class UserNotifierAdapter: UserNotifierPort {
    override suspend fun notifyUser(userNotificationDto: UserNotificationDto) {
        userNotificationDto.targetUsers.forEach {(targetUsers, jsonData) ->
            targetUsers.forEach {
                val connection = connections[it]
                connection?.send(
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



@Serializable
private data class UserNotification(
    val event: UserNotificationEvent,
    val jsonData: String?,
)