package com.finitas.domain.ports

import com.finitas.domain.dto.store.GetMessagesFromVersionDto
import com.finitas.domain.dto.store.SendMessageDto
import com.finitas.domain.dto.store.NewMessagesDto

interface RoomMessageRepository {
    suspend fun sendMessage(sendMessageDto: SendMessageDto): NewMessagesDto
    suspend fun getMessagesFromVersion(messagesFromVersionDto: GetMessagesFromVersionDto): NewMessagesDto
}