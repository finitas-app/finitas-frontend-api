package com.finitas.domain.dto.store

import com.finitas.config.serialization.LocalDateTimeSerializer
import com.finitas.config.serialization.SerializableUUID
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

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
    val visibleName: String,
)

@Serializable
data class UserDto(
    val idUser: SerializableUUID,
    val visibleName: String,
    val regularSpendings: List<RegularSpendingDto>
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
    @Serializable(with = LocalDateTimeSerializer::class)
    val lastActualizationDate: LocalDateTime,
    val spendingSummary: SpendingSummaryDto,
)