package com.finitas.domain.services

import com.finitas.domain.api.SyncMessagesFromVersionResponse
import com.finitas.domain.dto.store.GetMessagesFromVersionDto
import com.finitas.domain.dto.store.NewMessagesDto
import com.finitas.domain.dto.store.SendMessageDto
import com.finitas.domain.ports.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RoomMessageService(
    private val roomMessageRepository: RoomMessageRepository,
    private val notifierPort: UserNotifierPort,
) {
    suspend fun sendMessages(sendMessageDto: SendMessageDto) {
        val response = roomMessageRepository.sendMessage(sendMessageDto)
        notifierPort.notifyUser(
            UserNotificationDto(
                event = UserNotificationEvent.SYNC_MESSAGE,
                targetUsers = response.messages.map {
                    TargetUsersNotificationDto(
                        it.users,
                        Json.encodeToString(it.messages)
                    )
                }
            )
        )
    }

    suspend fun getNewMessages(getMessagesFromVersionDto: GetMessagesFromVersionDto): SyncMessagesFromVersionResponse {
        return roomMessageRepository.getMessagesFromVersion(getMessagesFromVersionDto)
            .messages
            .filter { getMessagesFromVersionDto.idUser in it.users }
            .flatMap { it.messages }
            .let { SyncMessagesFromVersionResponse(it) }
    }
}
