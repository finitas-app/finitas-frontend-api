package com.finitas.domain.ports

import com.finitas.config.serialization.SerializableUUID
import com.finitas.domain.dto.store.CreateRoomDto
import com.finitas.domain.dto.store.RoomDto
import com.finitas.domain.dto.store.RoomVersionDto
import kotlinx.serialization.Serializable

interface RoomRepository {

    suspend fun createRoom(createRoomDto: CreateRoomDto): RoomDto

    suspend fun getRoomFromVersion(getRoomsFromVersionsDto: GetRoomsFromVersionsDto): SyncRoomsResponse

}

@Serializable
data class SyncRoomsResponse(
    val rooms: List<RoomDto>,
    val unavailableRooms: List<SerializableUUID>,
)

@Serializable
data class GetRoomsFromVersionsDto(
    val idUser: SerializableUUID,
    val roomVersions: List<RoomVersionDto>,
)