package com.finitas.adapters

import com.finitas.config.httpClient
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.dto.store.*
import com.finitas.domain.ports.FinishedSpendingStoreRepository
import io.ktor.client.call.*
import io.ktor.client.request.*

class FinishedSpendingStoreRepositoryImpl(urlProvider: UrlProvider) : FinishedSpendingStoreRepository {

    private val url = "${urlProvider.STORE_HOST_URL}/finished-spendings"

    override suspend fun synchronizeFinishedSpendings(request: SynchronizationRequest<FinishedSpendingDto>): SynchronizationResponse<FinishedSpendingDto> {
        return httpClient.put("$url/synchronize") {
            setBody(request)
            headers {
                append("Content-Type", "application/json")
            }
        }.body()
    }

    override suspend fun getAllFinishedSpendings(idUser: String): List<FinishedSpendingDto> {
        return httpClient.get("$url/$idUser") {
            headers {
                append("Content-Type", "application/json")
            }
        }.body()
    }

    override suspend fun createFinishedSpending(dto: FinishedSpendingDto): UpdateResponse {
        return httpClient.post(url) {
            setBody(dto)
            headers {
                append("Content-Type", "application/json")
            }
        }.body()
    }

    override suspend fun updateFinishedSpending(dto: FinishedSpendingDto): UpdateResponse {
        return httpClient.patch(url) {
            setBody(dto)
            headers {
                append("Content-Type", "application/json")
            }
        }.body()
    }

    override suspend fun deleteFinishedSpending(request: DeleteFinishedSpendingRequest): UpdateResponse {
        return httpClient.delete(url) {
            setBody(request)
            headers {
                append("Content-Type", "application/json")
            }
        }.body()
    }
}
