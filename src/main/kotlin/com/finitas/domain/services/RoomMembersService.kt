package com.finitas.domain.services

import com.finitas.domain.api.AssignRoleToUserRequest
import com.finitas.domain.api.DeleteUserRequest
import com.finitas.domain.ports.*
import java.util.*

class RoomMembersService(
    private val roomMembersRepository: RoomMembersRepository,
    private val notifierPort: UserNotifierPort,
) {
    suspend fun addUserToRoomWithInvitationLink(joinRoomWithInvitationDto: JoinRoomWithInvitationDto) {
        val response = roomMembersRepository.addUserToRoomWithInvitationLink(joinRoomWithInvitationDto)
        //todo: notify partly but not whole room
        notifierPort.notifyUser(
            UserNotificationDto(
                UserNotificationEvent.SYNC_ROOM,
                listOf(TargetUsersNotificationDto(response.usersToNotify))
            )
        )
    }

    suspend fun deleteUserFromRoom(
        requester: UUID,
        deleteUserRequest: DeleteUserRequest,
    ) {
        val response = roomMembersRepository.deleteUserFromRoom(requester, deleteUserRequest)
        //todo: notify partly but not whole room
        notifierPort.notifyUser(
            UserNotificationDto(
                UserNotificationEvent.SYNC_ROOM,
                listOf(TargetUsersNotificationDto(response.usersToNotify))
            )
        )
    }

    suspend fun assignRoleToUser(
        requester: UUID,
        assignRoleToUserRequest: AssignRoleToUserRequest,
    ) {
        val response = roomMembersRepository.assignRoleToUser(requester, assignRoleToUserRequest)
        //todo: notify partly but not whole room
        notifierPort.notifyUser(
            UserNotificationDto(
                UserNotificationEvent.SYNC_ROOM,
                listOf(TargetUsersNotificationDto(response.usersToNotify))
            )
        )
    }
}