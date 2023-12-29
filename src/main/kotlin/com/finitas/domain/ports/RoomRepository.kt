package com.finitas.domain.ports

import com.finitas.adapters.ChangeRoomNameRequest
import com.finitas.adapters.RegenerateLinkResponse
import com.finitas.config.serialization.SerializableUUID
import com.finitas.domain.dto.store.CreateRoomDto
import com.finitas.domain.dto.store.RoomDto
import com.finitas.domain.dto.store.RoomVersionDto
import com.finitas.domain.dto.store.UsersToNotifyResponse
import kotlinx.serialization.Serializable
import java.util.*

interface RoomRepository {

    suspend fun createRoom(createRoomDto: CreateRoomDto): RoomDto

    suspend fun getRoomFromVersion(getRoomsFromVersionsDto: GetRoomsFromVersionsDto): SyncRoomsResponse

    suspend fun regenerateInvitationLink(requester: UUID, idRoom: UUID): RegenerateLinkResponse
    suspend fun changeRoomName(
        requester: UUID,
        idRoom: UUID,
        changeRoomNameRequest: ChangeRoomNameRequest
    ): UsersToNotifyResponse
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
