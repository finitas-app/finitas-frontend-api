package com.finitas.domain.services

import com.finitas.domain.dto.store.*
import com.finitas.domain.ports.FinishedSpendingStoreRepository
import java.util.*

class FinishedSpendingStoreService(private val repository: FinishedSpendingStoreRepository) {
    suspend fun synchronizeFinishedSpendings(request: SynchronizationRequest<FinishedSpendingDto>) =
        repository.synchronizeFinishedSpendings(request)

    suspend fun getAllFinishedSpendings(idUser: UUID) = repository.getAllFinishedSpendings(idUser)
    suspend fun createFinishedSpending(dto: FinishedSpendingDto) =
        repository.createFinishedSpending(dto)

    suspend fun updateFinishedSpending(dto: FinishedSpendingDto) =
        repository.updateFinishedSpending(dto)

    suspend fun deleteFinishedSpending(request: DeleteFinishedSpendingRequest) =
        repository.deleteFinishedSpending(request)

    suspend fun updateWithChangedItems(request: List<FinishedSpendingDto>, petitioner: UUID) =
        repository.updateWithChangedItems(
            IdUserWithEntities(
                idUser = petitioner,
                changedValues = request
            )
        )

    suspend fun fetchUsersUpdates(request: List<IdUserWithVersion>) = repository.fetchUsersUpdates(request)
}