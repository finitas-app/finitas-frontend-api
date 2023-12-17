package com.finitas.domain.dto.store

import com.finitas.config.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserIdValue(
    @Serializable(UUIDSerializer::class)
    val userId: UUID,
)

@Serializable
data class GetVisibleNamesRequest(
    val userIds: List<UserIdValue>
)

@Serializable
data class IdUserWithVisibleName(
    @Serializable(UUIDSerializer::class)
    val idUser: UUID,
    val visibleName: String,
)

@Serializable
data class UserDto(
    @Serializable(UUIDSerializer::class)
    val idUser: UUID,
    val visibleName: String,
    val regularSpendings: List<RegularSpendingDto>
)

@Serializable
data class VisibleName(
    val value: String
)

@Serializable
data class IdRegularSpending(
    @Serializable(UUIDSerializer::class)
    val id: UUID
)

@Serializable
data class UserDataDto(
    val visibleName: String,
    val regularSpendings: List<RegularSpendingDto>
)

@Serializable
data class RegularSpendingDto(
    @Serializable(UUIDSerializer::class)
    val idRegularSpending: UUID,
    val cron: String,
    val spendingSummary: SpendingSummaryDto,
)