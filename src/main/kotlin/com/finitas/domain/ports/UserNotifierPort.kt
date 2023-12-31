package com.finitas.domain.ports

import com.finitas.config.serialization.SerializableUUID

interface UserNotifierPort {
    suspend fun notifyUser(userNotificationDto: UserNotificationDto)
}


data class UserNotificationDto(
    val event: UserNotificationEvent,
    val targetUsers: List<TargetUsersNotificationDto>,
)

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
}