package com.finitas.adapters

import com.finitas.config.httpClient
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.dto.store.CreateRoomDto
import com.finitas.domain.dto.store.RoomDto
import com.finitas.domain.ports.GetRoomsFromVersionsDto
import com.finitas.domain.ports.RoomRepository
import com.finitas.domain.ports.SyncRoomsResponse
import io.ktor.client.call.*
import io.ktor.client.request.*

class RoomRepositoryImpl(private val urlProvider: UrlProvider) : RoomRepository {
    override suspend fun createRoom(createRoomDto: CreateRoomDto): RoomDto {
        return httpClient.post("${urlProvider.ROOM_MANAGER_HOST_URL}/rooms") {
            setBody(createRoomDto)
        }.body()
    }

    override suspend fun getRoomFromVersion(getRoomsFromVersionsDto: GetRoomsFromVersionsDto): SyncRoomsResponse {
        return httpClient.post("${urlProvider.ROOM_MANAGER_HOST_URL}/rooms/sync") {
            setBody(getRoomsFromVersionsDto)
        }.body()
    }
}

