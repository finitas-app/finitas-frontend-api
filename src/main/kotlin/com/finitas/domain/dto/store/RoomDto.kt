package com.finitas.domain.dto.store

import com.finitas.config.serialization.SerializableUUID
import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomDto(
    val creator: SerializableUUID,
    val roomName: String,
)

@Serializable
data class RoomDto(
    val idRoom: SerializableUUID,
    val name: String,
    val idInvitationLink: SerializableUUID,
    val version: Int,
    val roles: List<RoomRoleDto>,
    val members: List<RoomMember>,
)

@Serializable
data class RoomRoleDto(
    val idRole: SerializableUUID,
    val name: String,
    val authorities: Set<Authority>,
)

@Serializable
data class RoomMember(
    val idUser: SerializableUUID,
    val idRole: SerializableUUID? = null,
)

enum class Authority {
    READ_USERS_DATA,
    MODIFY_USERS_DATA,
    MODIFY_ROOM,
}

@Serializable
data class RoomVersionDto(
    val idRoom: SerializableUUID,
    val version: Int,
)

@Serializable
data class UsersToNotifyResponse(
    val usersToNotify: List<SerializableUUID>,
)
