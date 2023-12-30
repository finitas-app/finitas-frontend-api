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
    val idReceipt: SerializableUUID?,
    val purchaseDate: Int,
    override val version: Int,
    override val idUser: SerializableUUID,
    override val isDeleted: Boolean,
    val idSpendingSummary: SerializableUUID,
    val createdAt: Int,
    val name: String,
    val spendingRecords: List<SpendingRecordDto>,
): SynchronizableEntity