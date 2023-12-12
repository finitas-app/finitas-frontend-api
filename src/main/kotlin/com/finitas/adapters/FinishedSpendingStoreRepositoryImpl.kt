package com.finitas.adapters

import com.finitas.config.urls.UrlProvider
import com.finitas.domain.dto.store.*
import com.finitas.domain.ports.FinishedSpendingStoreRepository

class FinishedSpendingStoreRepositoryImpl(private val urlProvider: UrlProvider) : FinishedSpendingStoreRepository {
    override suspend fun synchronizeFinishedSpendings(request: SynchronizationRequest<FinishedSpendingDto>): SynchronizationResponse<FinishedSpendingDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllFinishedSpendings(idUser: String): List<FinishedSpendingDto> {
        TODO("Not yet implemented")
    }

    override suspend fun createFinishedSpending(dto: FinishedSpendingDto): UpdateResponse {
        TODO("Not yet implemented")
    }

    override suspend fun updateFinishedSpending(dto: FinishedSpendingDto): UpdateResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFinishedSpending(dto: DeleteFinishedSpendingRequest): UpdateResponse {
        TODO("Not yet implemented")
    }
}
