package com.finitas.domain.ports

import com.finitas.config.serialization.SerializableUUID
import com.finitas.domain.dto.store.CreateRoomDto
import com.finitas.domain.dto.store.RoomDto
import com.finitas.domain.dto.store.RoomVersionDto
import kotlinx.serialization.Serializable

interface RoomRepository {

    suspend fun createRoom(createRoomDto: CreateRoomDto): RoomDto

    suspend fun getRoomFromVersion(getRoomsFromVersionsDto: GetRoomsFromVersionsDto): List<RoomDto>
}

@Serializable
data class GetRoomsFromVersionsDto(
    val idUser: SerializableUUID,
    val roomVersions: List<RoomVersionDto>,
)