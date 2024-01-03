package com.finitas.domain.ports

import com.finitas.domain.dto.store.*
import java.util.*

interface FinishedSpendingStoreRepository {
    suspend fun synchronizeFinishedSpendings(request: SynchronizationRequest<FinishedSpendingDto>): SynchronizationResponse<FinishedSpendingDto>
    suspend fun getAllFinishedSpendings(idUser: UUID): List<FinishedSpendingDto>
    suspend fun createFinishedSpending(dto: FinishedSpendingDto): UpdateResponse
    suspend fun updateFinishedSpending(dto: FinishedSpendingDto): UpdateResponse
    suspend fun deleteFinishedSpending(request: DeleteFinishedSpendingRequest): UpdateResponse
    suspend fun updateWithChangedItems(idUserWithEntities: IdUserWithEntities<FinishedSpendingDto>)
    suspend fun fetchUsersUpdates(request: List<IdUserWithVersion>): List<FetchUpdatesResponse<FinishedSpendingDto>>
}