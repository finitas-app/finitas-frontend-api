package com.finitas.domain.ports

import com.finitas.domain.dto.store.*

interface FinishedSpendingStoreRepository {
    suspend fun synchronizeFinishedSpendings(request: SynchronizationRequest<FinishedSpendingDto>): SynchronizationResponse<FinishedSpendingDto>
    suspend fun getAllFinishedSpendings(idUser: String): List<FinishedSpendingDto>
    suspend fun createFinishedSpending(dto: FinishedSpendingDto): UpdateResponse
    suspend fun updateFinishedSpending(dto: FinishedSpendingDto): UpdateResponse
    suspend fun deleteFinishedSpending(dto: DeleteFinishedSpendingRequest): UpdateResponse
}