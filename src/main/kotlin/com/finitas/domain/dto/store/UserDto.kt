package com.finitas.domain.dto.store

import com.finitas.config.serialization.SerializableUUID
import kotlinx.serialization.Serializable

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
data class IdRegularSpending(
    val id: SerializableUUID,
)

@Serializable
data class UserDataDto(
    val visibleName: String,
    val regularSpendings: List<RegularSpendingDto>
)

@Serializable
data class RegularSpendingDto(
    val idRegularSpending: SerializableUUID,
    val cron: String,
    val spendingSummary: SpendingSummaryDto,
)