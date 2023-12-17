package com.finitas.domain.dto.store

import com.finitas.config.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

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
    @Serializable(UUIDSerializer::class)
    val idSpendingRecordData: UUID,
    val name: String,
    // todo: serialize as big decimal
    val price: Double,
    val category: CategoryDto,
)

@Serializable
data class CategoryDto(
    @Serializable(UUIDSerializer::class)
    val idCategory: UUID,
    val name: String,
    @Serializable(UUIDSerializer::class)
    val idParent: UUID?,
)
