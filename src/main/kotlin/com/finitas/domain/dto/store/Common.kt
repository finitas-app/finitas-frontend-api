package com.finitas.domain.dto.store

import com.finitas.config.exceptions.BadRequestException
import com.finitas.config.exceptions.ErrorCode
import com.finitas.config.serialization.SerializableBigDecimal
import com.finitas.config.serialization.SerializableUUID
import kotlinx.serialization.Serializable
import java.util.*

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
data class SynchronizationRequestFromClient<T : SynchronizableEntity>(
    val lastSyncVersion: Int,
    val idUser: SerializableUUID?,
    val objects: List<T>
) {

    init {
        if (objects.map { it.idUser }.toSet().size > 1) {
            throw BadRequestException(
                "Trying to synchronize items for more than one user",
                ErrorCode.TRIED_SYNC_BY_MORE_THAN_ONE_USER
            )
        }
    }
    fun mapToStoreRequest(petitioner: UUID) = SynchronizationRequest(
        lastSyncVersion = lastSyncVersion,
        idUser = idUser ?: petitioner,
        objects = objects,
        isAuthorDataToUpdate = idUser == petitioner
    )
}

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
