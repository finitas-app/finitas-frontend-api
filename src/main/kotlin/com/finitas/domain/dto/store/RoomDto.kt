package com.finitas.domain.dto.store

import com.finitas.config.serialization.SerializableUUID
import com.finitas.config.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class CreateRoomDto(
    @Serializable(UUIDSerializer::class)
    val creator: UUID,
    val roomName: String,
)

@Serializable
data class RoomDto(
    @Serializable(UUIDSerializer::class)
    val idRoom: UUID,
    val name: String,
    @Serializable(UUIDSerializer::class)
    val idInvitationLink: UUID,
    val version: Int,
    val roles: List<RoomRoleDto>,
    val members: List<RoomMember>,
)

@Serializable
data class RoomRoleDto(
    @Serializable(UUIDSerializer::class)
    val idRole: UUID,
    val name: String,
    val authorities: Set<Authority>,
)

@Serializable
data class RoomMember(
    @Serializable(UUIDSerializer::class)
    val idUser: UUID,
    val roomRole: RoomRoleDto? = null,
)

enum class Authority {
    READ_USERS_DATA,
    MODIFY_USERS_DATA,
    MODIFY_ROOM,
    MODIFY
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
