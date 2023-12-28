package com.finitas.adapters

import com.finitas.config.httpClient
import com.finitas.config.serialization.SerializableUUID
import com.finitas.config.setRoomAuthorization
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.api.AssignRoleToUserRequest
import com.finitas.domain.api.DeleteUserRequest
import com.finitas.domain.dto.store.RoomDto
import com.finitas.domain.dto.store.UsersToNotifyResponse
import com.finitas.domain.ports.JoinRoomWithInvitationDto
import com.finitas.domain.ports.ReachableUsersDto
import com.finitas.domain.ports.RoomMembersRepository
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import java.util.*

class RoomMembersRepositoryImpl(
    private val urlProvider: UrlProvider,
) : RoomMembersRepository {
    override suspend fun getReachableUsersForUser(idUser: UUID, idRoom: UUID?): ReachableUsersDto {
        return httpClient.get("${urlProvider.ROOM_MANAGER_HOST_URL}/rooms/users") {
            parameter("idUser", idUser)
            parameter("idRoom", idRoom)
        }.body()
    }

    override suspend fun addUserToRoomWithInvitationLink(joinRoomWithInvitationDto: JoinRoomWithInvitationDto): UsersToNotifyResponse {
        return httpClient.post("${urlProvider.ROOM_MANAGER_HOST_URL}/rooms/users") {
            setBody(joinRoomWithInvitationDto)
        }.body()
    }

    override suspend fun deleteUserFromRoom(
        requester: UUID,
        deleteUserRequest: DeleteUserRequest,
    ): UsersToNotifyResponse {
        return httpClient.delete("${urlProvider.ROOM_MANAGER_HOST_URL}/rooms/users") {
            setRoomAuthorization(requester, deleteUserRequest.idRoom)
            setBody(deleteUserRequest.toDto())
        }.body()
    }

    override suspend fun assignRoleToUser(
        requester: UUID,
        assignRoleToUserRequest: AssignRoleToUserRequest
    ): UsersToNotifyResponse {
        return httpClient.put("${urlProvider.ROOM_MANAGER_HOST_URL}/rooms/users/roles") {
            setRoomAuthorization(requester, assignRoleToUserRequest.idRoom)
            setBody(assignRoleToUserRequest.toDto())
        }.body()
    }
}

@Serializable
data class DeleteUserDto(
    val idUser: SerializableUUID,
)

@Serializable
data class AssignRoleToUserDto(
    val idRole: SerializableUUID?,
    val idUser: SerializableUUID,
)
