package com.finitas.adapters

import com.finitas.config.contentTypeJson
import com.finitas.config.httpClient
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.dto.store.*
import com.finitas.domain.ports.FinishedSpendingStoreRepository
import io.ktor.client.call.*
import io.ktor.client.request.*
import java.util.*

class FinishedSpendingStoreRepositoryImpl(urlProvider: UrlProvider) : FinishedSpendingStoreRepository {

    private val url = "${urlProvider.STORE_HOST_URL}/finished-spendings"

    override suspend fun synchronizeFinishedSpendings(request: SynchronizationRequest<FinishedSpendingDto>): SynchronizationResponse<FinishedSpendingDto> {
        return httpClient.put("$url/synchronize") {
            setBody(request)
            contentTypeJson()
        }.body()
    }

    override suspend fun getAllFinishedSpendings(idUser: UUID): List<FinishedSpendingDto> {
        return httpClient.get("$url/$idUser") {
            contentTypeJson()
        }.body()
    }

    override suspend fun createFinishedSpending(dto: FinishedSpendingDto): UpdateResponse {
        return httpClient.post(url) {
            contentTypeJson()
            setBody(dto)
        }.body()
    }

    override suspend fun updateFinishedSpending(dto: FinishedSpendingDto): UpdateResponse {
        return httpClient.patch(url) {
            contentTypeJson()
            setBody(dto)
        }.body()
    }

    override suspend fun deleteFinishedSpending(request: DeleteFinishedSpendingRequest): UpdateResponse {
        return httpClient.delete(url) {
            contentTypeJson()
            setBody(request)
        }.body()
    }
}
