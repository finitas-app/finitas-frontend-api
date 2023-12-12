package com.finitas.domain.dto.store

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.math.BigDecimal

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
    val idSpendingRecordData: String,
    val name: String,
    @Contextual //todo: check if working
    val price: BigDecimal,
    val category: CategoryDto,
)

@Serializable
data class CategoryDto(
    val idCategory: String,
    val name: String,
    val idParent: String?,
)
