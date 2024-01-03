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
                UserNotificationEvent.ADD_USER_TO_ROOM,
                //TODO: add some info to jsonData to optimize sync process
                listOf(TargetUsersNotificationDto(response.usersToNotify))
            )
        )
    }

    suspend fun deleteUserFromRoom(
        requester: UUID,
        deleteUserRequest: DeleteUserRequest,
    ) {
        val response = roomMembersRepository.deleteUserFromRoom(requester, deleteUserRequest)
        notifierPort.notifyUser(
            UserNotificationDto(
                UserNotificationEvent.DELETE_USER_FROM_ROOM,
                //TODO: add some info to jsonData to optimize sync process
                listOf(TargetUsersNotificationDto(response.usersToNotify))
            )
        )
    }

    suspend fun assignRoleToUser(
        requester: UUID,
        assignRoleToUserRequest: AssignRoleToUserRequest,
    ) {
        val response = roomMembersRepository.assignRoleToUser(requester, assignRoleToUserRequest)
        notifierPort.notifyUser(
            UserNotificationDto(
                UserNotificationEvent.ASSIGN_ROLE_TO_USER,
                //TODO: add some info to jsonData to optimize sync process
                listOf(TargetUsersNotificationDto(response.usersToNotify))
            )
        )
    }
}