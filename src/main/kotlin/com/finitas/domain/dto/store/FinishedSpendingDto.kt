package com.finitas.domain.dto.store

import com.finitas.config.serialization.SerializableUUID
import kotlinx.serialization.Serializable

@Serializable
data class DeleteFinishedSpendingRequest(
    val idSpendingSummary: SerializableUUID,
    val idUser: SerializableUUID,
)

@Serializable
class FinishedSpendingDto(
    val spendingSummary: SpendingSummaryDto,
    val idReceipt: SerializableUUID?,
    val purchaseDate: Int,
    val version: Int,
    val idUser: SerializableUUID,
    val isDeleted: Boolean,
)

@Serializable
data class SpendingSummaryDto(
    val idSpendingSummary: SerializableUUID,
    val createdAt: Int,
    val name: String,
    val spendingRecords: List<SpendingRecordDto>,
)

@Serializable
data class SpendingRecordDto(
    val idSpendingRecord: SerializableUUID,
    val spendingRecordData: SpendingRecordDataDto,
)