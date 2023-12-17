package com.finitas.domain.dto.store

import com.finitas.config.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class DeleteFinishedSpendingRequest(
    @Serializable(UUIDSerializer::class)
    val idSpendingSummary: UUID,
    @Serializable(UUIDSerializer::class)
    val idUser: UUID,
)

@Serializable
class FinishedSpendingDto(
    val spendingSummary: SpendingSummaryDto,
    @Serializable(UUIDSerializer::class)
    val idReceipt: UUID?,
    val purchaseDate: Int,
    val version: Int,
    @Serializable(UUIDSerializer::class)
    val idUser: UUID,
    val isDeleted: Boolean,
)

@Serializable
data class SpendingSummaryDto(
    @Serializable(UUIDSerializer::class)
    val idSpendingSummary: UUID,
    val createdAt: Int,
    val name: String,
    val spendingRecords: List<SpendingRecordDto>,
)

@Serializable
data class SpendingRecordDto(
    @Serializable(UUIDSerializer::class)
    val idSpendingRecord: UUID,
    val spendingRecordData: SpendingRecordDataDto,
)