package com.finitas.domain.dto.store

import com.finitas.config.serialization.SerializableBigDecimal
import com.finitas.config.serialization.SerializableUUID
import kotlinx.serialization.Serializable

@Serializable
data class IdUserWithEntities<T> (
    val idUser: SerializableUUID,
    val changedValues: List<T>
)

@Serializable
data class IdUserWithVisibleName(
    val idUser: SerializableUUID,
    val visibleName: String?,
)

@Serializable
data class IdUserWithVersion(
    val idUser: SerializableUUID,
    val version: Int
)

@Serializable
data class FetchUpdatesResponse<T>(
    val updates: List<T>,
    val idUser: SerializableUUID,
    val actualVersion: Int,
)

@Serializable
data class ResponseMessage(val message: String)

@Serializable
data class UpdateResponse(val lastSyncVersion: Int)

interface SynchronizableEntity {
    val version: Int
    val idUser: SerializableUUID
    val isDeleted: Boolean
}

@Serializable
data class SynchronizationRequest<T : SynchronizableEntity>(
    val lastSyncVersion: Int,
    val idUser: SerializableUUID,
    val isAuthorDataToUpdate: Boolean,
    val objects: List<T>
)

@Serializable
data class SynchronizationResponse<T>(
    val actualizedSyncVersion: Int,
    val objects: List<T>
)

@Serializable
data class SpendingRecordDto(
    val idSpendingRecord: SerializableUUID,
    val idSpendingRecordData: SerializableUUID,
    val name: String,
    val price: SerializableBigDecimal,
    val idCategory: SerializableUUID,
)
