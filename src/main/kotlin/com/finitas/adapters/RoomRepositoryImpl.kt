package com.finitas.adapters

import com.finitas.config.httpClient
import com.finitas.config.serialization.SerializableUUID
import com.finitas.config.setRoomAuthorization
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.dto.store.CreateRoomDto
import com.finitas.domain.dto.store.RoomDto
import com.finitas.domain.dto.store.UsersToNotifyResponse
import com.finitas.domain.ports.GetRoomsFromVersionsDto
import com.finitas.domain.ports.RoomRepository
import com.finitas.domain.ports.SyncRoomsResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import java.util.*

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

    override suspend fun regenerateInvitationLink(requester: UUID, idRoom: UUID): RegenerateLinkResponse {
        return httpClient.post("${urlProvider.ROOM_MANAGER_HOST_URL}/rooms/regenerate-link") {
            setRoomAuthorization(requester, idRoom)
        }.body()
    }

    override suspend fun changeRoomName(
        requester: UUID,
        idRoom: UUID,
        changeRoomNameRequest: ChangeRoomNameRequest
    ): UsersToNotifyResponse {
        return httpClient.patch("${urlProvider.ROOM_MANAGER_HOST_URL}/rooms/name") {
            setRoomAuthorization(requester, idRoom)
            setBody(changeRoomNameRequest)
        }.body()
    }
}

@Serializable
data class RegenerateLinkResponse(
    val invitationLinkUUID: SerializableUUID,
    val usersToNotify: List<SerializableUUID>,
)

@Serializable
data class ChangeRoomNameRequest(
    val newRoomName: String,
)

