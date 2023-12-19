package com.finitas.adapters

import com.finitas.config.httpClient
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.dto.store.GetMessagesFromVersionDto
import com.finitas.domain.dto.store.SendMessageDto
import com.finitas.domain.dto.store.NewMessagesDto
import com.finitas.domain.ports.RoomMessageRepository
import io.ktor.client.call.*
import io.ktor.client.request.*

class RoomMessageRepositoryImpl(
    private val urlProvider: UrlProvider,
): RoomMessageRepository {
    override suspend fun sendMessage(sendMessageDto: SendMessageDto): NewMessagesDto {
        return httpClient.post("${urlProvider.ROOM_MANAGER_HOST_URL}/messages") {
            setBody(sendMessageDto)
        }.body()
    }

    override suspend fun getMessagesFromVersion(messagesFromVersionDto: GetMessagesFromVersionDto): NewMessagesDto {
        return httpClient.post("${urlProvider.ROOM_MANAGER_HOST_URL}/messages/sync") {
            setBody(messagesFromVersionDto)
        }.body()
    }
}