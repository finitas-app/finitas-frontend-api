package com.finitas.domain.dto.store

import com.finitas.config.serialization.SerializableUUID
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class DeleteFinishedSpendingRequest(
    val idSpendingSummary: SerializableUUID,
    val idUser: SerializableUUID,
)

@Serializable
class FinishedSpendingDto(
    val idReceipt: SerializableUUID?,
    val purchaseDate: Int,
    val version: Int,
    val idUser: SerializableUUID,
    val isDeleted: Boolean,
    val idSpendingSummary: SerializableUUID,
    val createdAt: Int,
    val name: String,
    val spendingRecords: List<SpendingRecordDto>,
)