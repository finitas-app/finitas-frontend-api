package com.finitas.domain.services

import com.finitas.adapters.ChangeRoomNameRequest
import com.finitas.domain.dto.store.CreateRoomDto
import com.finitas.domain.dto.store.RoomDto
import com.finitas.domain.ports.*
import java.util.*

class RoomService(
    private val roomRepository: RoomRepository,
    private val userNotifierPort: UserNotifierPort,
) {
    suspend fun createRoom(createRoomDto: CreateRoomDto): RoomDto {
        return roomRepository.createRoom(createRoomDto)
    }

    suspend fun getRoomsFromVersions(getRoomsFromVersionsDto: GetRoomsFromVersionsDto): SyncRoomsResponse {
        return roomRepository.getRoomFromVersion(getRoomsFromVersionsDto)
    }

    suspend fun regenerateInvitationLink(requester: UUID, idRoom: UUID) {
        val response = roomRepository.regenerateInvitationLink(requester, idRoom)
        userNotifierPort.notifyUser(
            UserNotificationDto(
                UserNotificationEvent.REGENERATE_INVITATION_LINK,
                targetUsers = listOf(TargetUsersNotificationDto(response.usersToNotify, null))
            )
        )
    }

    suspend fun changeRoomName(requester: UUID, idRoom: UUID, changeRoomNameRequest: ChangeRoomNameRequest) {
        val response = roomRepository.changeRoomName(requester, idRoom, changeRoomNameRequest)
        userNotifierPort.notifyUser(
            UserNotificationDto(
                UserNotificationEvent.CHANGE_ROOM_NAME,
                //TODO: add some info to jsonData to optimize sync process
                targetUsers = listOf(TargetUsersNotificationDto(response.usersToNotify, null))
            )
        )
    }
}


