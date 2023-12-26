package com.finitas.domain.model

import com.finitas.config.serialization.LocalDateTimeSerializer
import com.finitas.config.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.*

@Serializable
data class Message(
    @Serializable(with = UUIDSerializer::class)
    val idMessage: UUID,
    @Serializable(with = UUIDSerializer::class)
    val idUser: UUID,
    @Serializable(with = UUIDSerializer::class)
    val idRoom: UUID,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val version: Int,
    @Serializable(with = UUIDSerializer::class)
    val idShoppingList: UUID? = null,
    val content: String? = null,
)