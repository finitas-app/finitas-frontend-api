package com.finitas.domain.api

import com.finitas.domain.dto.store.*
import com.finitas.domain.model.Message
import com.finitas.domain.ports.GetRoomsFromVersionsDto
import com.finitas.domain.services.RoomMessageService
import com.finitas.domain.services.RoomService
import com.finitas.domain.utils.getPetitioner
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Route.roomRouter() {
    val roomService: RoomService by inject()
    route("/rooms") {
        messageRoute()
        post {
            val userId = call.getPetitioner()
            val response = roomService.createRoom(
                CreateRoomDto(
                    userId,
                    call.receive<CreateRoomRequest>().roomName,
                )
            )
            call.respond(HttpStatusCode.Created, response)
        }
        post("/sync") {
            val userId = call.getPetitioner()
            val response = roomService.getRoomsFromVersions(
                GetRoomsFromVersionsDto(
                    userId,
                    call.receive<GetRoomsFromVersionsRequest>().roomVersions,
                )
            )
            call.respond(HttpStatusCode.Created, response)
        }
    }
}

fun Route.messageRoute() {
    val roomMessageService: RoomMessageService by inject()
    route("/messages") {
        post {
            val userId = call.getPetitioner()
            roomMessageService.sendMessages(
                SendMessageDto(
                    userId,
                    call.receive<SendMessageRequest>().messages,
                )
            )
            call.respond(HttpStatusCode.NoContent)
        }
        post("/sync") {
            val userId = call.getPetitioner()
            val response = roomMessageService.getNewMessages(
                GetMessagesFromVersionDto(
                    userId,
                    call.receive<SyncMessagesFromVersionRequest>().lastMessagesVersions,
                )
            )
            call.respond(HttpStatusCode.OK, response)
        }
    }

}



@Serializable
data class SyncMessagesFromVersionRequest(
    val lastMessagesVersions: List<MessagesVersionDto>,
)

@Serializable
data class SyncMessagesFromVersionResponse(
    val messages: List<Message>,
)

@Serializable
data class SendMessageRequest(
    val messages: List<SingleMessageDto>,
)

@Serializable
data class CreateRoomRequest(
    val roomName: String,
)

@Serializable
data class GetRoomsFromVersionsRequest(
    val roomVersions: List<RoomVersionDto>,
)
