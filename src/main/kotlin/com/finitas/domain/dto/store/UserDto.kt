package com.finitas.domain.dto.store

import com.finitas.config.serialization.LocalDateTimeSerializer
import com.finitas.config.serialization.SerializableUUID
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.*

@Serializable
data class UserIdValue(
    val userId: SerializableUUID,
)

@Serializable
data class GetVisibleNamesRequest(
    val userIds: List<UserIdValue>
)

@Serializable
data class IdUserWithVisibleName(
    val idUser: SerializableUUID,
    val visibleName: String?,
)

@Serializable
data class IdUserWithVersion(
    val userId: SerializableUUID,
    val version: Int
)

@Serializable
data class UserDto(
    val idUser: SerializableUUID,
    val version: Int,
    val visibleName: String?,
    val regularSpendings: List<RegularSpendingDto>,
    val categories: List<CategoryDto>
)

@Serializable
data class VisibleName(
    val value: String
)

@Serializable
data class IdSpendingSummary(
    val idSpendingSummary: SerializableUUID,
)

@Serializable
data class RegularSpendingDto(
    val actualizationPeriod: Int,
    val periodUnit: Int,
    @Serializable(LocalDateTimeSerializer::class)
    val lastActualizationDate: LocalDateTime,
    val idSpendingSummary: SerializableUUID,
    val createdAt: Int,
    val name: String,
    val spendingRecords: List<SpendingRecordDto>,
)

@Serializable
data class CategoryDto(
    val idCategory: SerializableUUID,
    val name: String,
    val idParent: SerializableUUID?,
    val version: Int?,
    val isDeleted: Boolean,
)