package com.finitas.domain.api

import com.finitas.adapters.*
import com.finitas.config.exceptions.BadRequestException
import com.finitas.config.exceptions.ErrorCode
import com.finitas.config.serialization.SerializableUUID
import com.finitas.domain.dto.store.*
import com.finitas.domain.model.Message
import com.finitas.domain.ports.GetRoomsFromVersionsDto
import com.finitas.domain.ports.JoinRoomWithInvitationDto
import com.finitas.domain.services.RoomMembersService
import com.finitas.domain.services.RoomMessageService
import com.finitas.domain.services.RoomRolesService
import com.finitas.domain.services.RoomService
import com.finitas.domain.utils.getPetitioner
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject
import java.util.*

fun Route.roomRouter() {
    val roomService: RoomService by inject()
    route("/rooms") {
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
            call.respond(HttpStatusCode.OK, response)
        }
        post("/{idRoom}/regenerate-link") {
            val userId = call.getPetitioner()
            val idRoom = call.parameters["idRoom"]?.let { UUID.fromString(it) }
                ?: throw BadRequestException("idRoom not provided", ErrorCode.ID_ROOM_NOT_PROVIDED)
            roomService.regenerateInvitationLink(userId, idRoom)
            call.respond(HttpStatusCode.NoContent)
        }
        patch("/{idRoom}/name") {
            val userId = call.getPetitioner()
            val idRoom = call.parameters["idRoom"]?.let { UUID.fromString(it) }
                ?: throw BadRequestException("idRoom not provided", ErrorCode.ID_ROOM_NOT_PROVIDED)
            roomService.changeRoomName(userId, idRoom, call.receive())
            call.respond(HttpStatusCode.NoContent)
        }
        messageRoute()
        rolesRoute()
        roomMembersRoute()
    }
}

fun Route.messageRoute() {
    val roomMessageService: RoomMessageService by inject()
    route("/messages") {
        post {
            val userId = call.getPetitioner()
            val response = roomMessageService.sendMessages(
                SendMessageDto(
                    userId,
                    call.receive<SendMessageRequest>().messages,
                )
            )
            call.respond(HttpStatusCode.OK, response)
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

fun Route.rolesRoute() {
    val roomRolesService: RoomRolesService by inject()
    route("/roles") {
        post {
            val requester = call.getPetitioner()
            val request: AddRoleRequest = call.receive()
            roomRolesService.addRole(requester, request)
            call.respond(HttpStatusCode.NoContent)
        }
        put {
            val requester = call.getPetitioner()
            val request: UpdateRoleRequest = call.receive()
            roomRolesService.updateRole(requester, request)
            call.respond(HttpStatusCode.NoContent)
        }
        delete {
            val requester = call.getPetitioner()
            val request: DeleteRoleRequest = call.receive()
            roomRolesService.deleteRole(requester, request)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}

fun Route.roomMembersRoute() {
    val roomMembersService: RoomMembersService by inject()
    route("/users") {
        post {
            val requester = call.getPetitioner()
            val request: JoinRoomWithInvitationRequest = call.receive()
            roomMembersService.addUserToRoomWithInvitationLink(request.toDto(requester))
            call.respond(HttpStatusCode.NoContent)
        }
        delete {
            val requester = call.getPetitioner()
            val request: DeleteUserRequest = call.receive()
            roomMembersService.deleteUserFromRoom(requester, request)
            call.respond(HttpStatusCode.NoContent)
        }
        put("/roles") {
            val requester = call.getPetitioner()
            val request: AssignRoleToUserRequest = call.receive()
            roomMembersService.assignRoleToUser(requester, request)
            call.respond(HttpStatusCode.NoContent)
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
    val unavailableRooms: List<SerializableUUID>,
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

@Serializable
data class AddRoleRequest(
    val idRoom: SerializableUUID,
    val name: String,
    val authorities: Set<Authority>,
) {
    fun toDto() = AddRoleDto(name, authorities)
}

@Serializable
data class UpdateRoleRequest(
    val idRoom: SerializableUUID,
    val idRole: SerializableUUID,
    val name: String,
    val authorities: Set<Authority>,
) {
    fun toDto() = UpdateRoleDto(idRole, name, authorities)
}

@Serializable
data class DeleteRoleRequest(
    val idRoom: SerializableUUID,
    val idRole: SerializableUUID,
) {
    fun toDto() = DeleteRoleDto(idRole)
}

@Serializable
data class JoinRoomWithInvitationRequest(
    val idInvitationLink: SerializableUUID,
) {
    fun toDto(idUser: UUID) = JoinRoomWithInvitationDto(idUser, idInvitationLink)
}

@Serializable
data class DeleteUserRequest(
    val idRoom: SerializableUUID,
    val idUser: SerializableUUID,
) {
    fun toDto() = DeleteUserDto(idUser)
}

@Serializable
data class AssignRoleToUserRequest(
    val idRoom: SerializableUUID,
    val idRole: SerializableUUID?,
    val idUser: SerializableUUID,
) {
    fun toDto() = AssignRoleToUserDto(idRole, idUser)
}
