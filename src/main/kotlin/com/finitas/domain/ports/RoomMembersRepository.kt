package com.finitas.domain.ports

import com.finitas.config.serialization.SerializableUUID
import com.finitas.domain.api.AssignRoleToUserRequest
import com.finitas.domain.api.DeleteUserRequest
import com.finitas.domain.dto.store.UsersToNotifyResponse
import kotlinx.serialization.Serializable
import java.util.*

interface RoomMembersRepository {

    suspend fun getReachableUsersForUser(idUser: UUID, idRoom: UUID?): ReachableUsersDto
    suspend fun addUserToRoomWithInvitationLink(joinRoomWithInvitationDto: JoinRoomWithInvitationDto): UsersToNotifyResponse
    suspend fun deleteUserFromRoom(
        requester: UUID,
        deleteUserRequest: DeleteUserRequest,
    ): UsersToNotifyResponse

    suspend fun assignRoleToUser(
        requester: UUID,
        assignRoleToUserRequest: AssignRoleToUserRequest,
    ): UsersToNotifyResponse
}

@Serializable
data class ReachableUsersDto(
    val reachableUsers: List<SerializableUUID>,
)

@Serializable
data class JoinRoomWithInvitationDto(
    val idUser: SerializableUUID,
    val idInvitationLink: SerializableUUID,
)
