package com.finitas.domain.dto.store

import com.finitas.config.serialization.SerializableUUID
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMessage(val message: String)

@Serializable
data class UpdateResponse(val lastSyncVersion: Int)

@Serializable
data class SynchronizationRequest<T>(
    val lastSyncVersion: Int,
    val isAuthorDataToUpdate: Boolean,
    val objects: List<T>
)

@Serializable
data class SynchronizationResponse<T>(
    val actualizedSyncVersion: Int,
    val objects: List<T>
)

@Serializable
data class SpendingRecordDataDto(
    val idSpendingRecordData: SerializableUUID,
    val name: String,
    // todo: serialize as big decimal
    val price: Double,
    val category: CategoryDto,
)

@Serializable
data class CategoryDto(
    val idCategory: SerializableUUID,
    val name: String,
    val idParent: SerializableUUID?,
)
