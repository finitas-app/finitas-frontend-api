package com.finitas.domain.ports

import com.finitas.domain.api.SyncMessagesFromVersionResponse
import com.finitas.domain.dto.store.GetMessagesFromVersionDto
import com.finitas.domain.dto.store.NewMessagesDto
import com.finitas.domain.dto.store.SendMessageDto

interface RoomMessageRepository {
    suspend fun sendMessage(sendMessageDto: SendMessageDto): NewMessagesDto
    suspend fun getMessagesFromVersion(messagesFromVersionDto: GetMessagesFromVersionDto): SyncMessagesFromVersionResponse
}