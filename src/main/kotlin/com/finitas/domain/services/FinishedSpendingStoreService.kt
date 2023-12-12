package com.finitas.domain.services

import com.finitas.domain.dto.store.DeleteFinishedSpendingRequest
import com.finitas.domain.dto.store.FinishedSpendingDto
import com.finitas.domain.dto.store.SynchronizationRequest
import com.finitas.domain.ports.FinishedSpendingStoreRepository

class FinishedSpendingStoreService(private val repository: FinishedSpendingStoreRepository) {
    suspend fun synchronizeFinishedSpendings(request: SynchronizationRequest<FinishedSpendingDto>) =
        repository.synchronizeFinishedSpendings(request)

    suspend fun getAllFinishedSpendings(idUser: String) = repository.getAllFinishedSpendings(idUser)
    suspend fun createFinishedSpending(dto: FinishedSpendingDto) =
        repository.createFinishedSpending(dto)

    suspend fun updateFinishedSpending(dto: FinishedSpendingDto) =
        repository.updateFinishedSpending(dto)

    suspend fun deleteFinishedSpending(dto: DeleteFinishedSpendingRequest) =
        repository.deleteFinishedSpending(dto)
}