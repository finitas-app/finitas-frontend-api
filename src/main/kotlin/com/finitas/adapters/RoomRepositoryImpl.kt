package com.finitas.adapters

import com.finitas.config.httpClient
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.dto.store.CreateRoomDto
import com.finitas.domain.dto.store.RoomDto
import com.finitas.domain.ports.GetRoomsFromVersionsDto
import com.finitas.domain.ports.ReachableUsersDto
import com.finitas.domain.ports.RoomRepository
import io.ktor.client.call.*
import io.ktor.client.request.*
import java.util.*

class RoomRepositoryImpl(private val urlProvider: UrlProvider): RoomRepository {
    override suspend fun createRoom(createRoomDto: CreateRoomDto): RoomDto {
        return httpClient.post("${urlProvider.ROOM_MANAGER_HOST_URL}/rooms") {
            setBody(createRoomDto)
        }.body()
    }

    override suspend fun getRoomFromVersion(getRoomsFromVersionsDto: GetRoomsFromVersionsDto): List<RoomDto> {
        return httpClient.post("${urlProvider.ROOM_MANAGER_HOST_URL}/rooms/sync") {
            setBody(getRoomsFromVersionsDto)
        }.body()
    }

    override suspend fun getReachableUsersForUser(idUser: UUID, idRoom: UUID?): ReachableUsersDto {
        return httpClient.get("${urlProvider.ROOM_MANAGER_HOST_URL}/rooms/users") {
            parameter("idUser", idUser)
            parameter("idRoom", idRoom)
        }.body()
    }
}

