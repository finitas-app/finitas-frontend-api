package com.finitas.domain.ports

import com.finitas.config.serialization.SerializableUUID
import kotlinx.serialization.Serializable

interface UserNotifierPort {
    suspend fun notifyUser(userNotificationDto: UserNotificationDto)
}

@Serializable
data class UserNotificationDto(
    val event: UserNotificationEvent,
    val targetUsers: List<TargetUsersNotificationDto>,
)

@Serializable
data class TargetUsersNotificationDto(
    val targetUsers: List<SerializableUUID>,
    val jsonData: String? = null,
)

enum class UserNotificationEvent {
    SYNC_MESSAGE,
    SYNC_ROOM,
    USERNAME_CHANGE,
    CATEGORY_CHANGED,
    SHOPPING_LIST_CHANGED,
    REGENERATE_INVITATION_LINK,
    CHANGE_ROOM_NAME,
    ADD_USER_TO_ROOM,
    DELETE_USER_FROM_ROOM,
    ASSIGN_ROLE_TO_USER,
    FINISHED_SPENDING_CHANGED,
}