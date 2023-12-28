package com.finitas.domain.dto.store

import com.finitas.config.serialization.SerializableUUID
import com.finitas.domain.model.Message
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageDto(
    val idUser: SerializableUUID,
    val messages: List<SingleMessageDto>,
)

@Serializable
data class MessagesVersionDto(
    val idRoom: SerializableUUID,
    val version: Int,
)

@Serializable
data class SingleMessageDto(
    val idMessage: SerializableUUID,
    val idRoom: SerializableUUID,
    val idShoppingList: SerializableUUID? = null,
    val content: String? = null,
)

@Serializable
data class NewMessagesDto(
    val messages: List<MessagesForUsers>,
    val unavailableRooms: List<SerializableUUID>,
)

@Serializable
data class MessagesForUsers(
    val users: List<SerializableUUID>,
    val messages: List<Message>
)

@Serializable
data class GetMessagesFromVersionDto(
    val idUser: SerializableUUID,
    val lastMessagesVersions: List<MessagesVersionDto>,
)
