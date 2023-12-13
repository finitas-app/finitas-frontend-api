package com.finitas.adapters

import com.finitas.config.httpClient
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.dto.store.*
import com.finitas.domain.ports.ShoppingListStoreRepository
import io.ktor.client.call.*
import io.ktor.client.request.*

class ShoppingListStoreRepositoryImpl(private val urlProvider: UrlProvider) : ShoppingListStoreRepository {

    private val url = "${urlProvider.STORE_HOST_URL}/shopping-lists"

    override suspend fun synchronizeShoppingLists(request: SynchronizationRequest<ShoppingListDto>): SynchronizationResponse<ShoppingListDto> {
        return httpClient.put("$url/synchronize") {
            setBody(request)
            headers {
                append("Content-Type", "application/json")
            }
        }.body()
    }

    override suspend fun getAllShoppingLists(idUser: String): List<ShoppingListDto> {
        return httpClient.get("$url/$idUser"){
            headers {
                append("Content-Type", "application/json")
            }
        }.body()
    }

    override suspend fun createShoppingList(dto: ShoppingListDto): UpdateResponse {
        return httpClient.post(url) {
            setBody(dto)
            headers {
                append("Content-Type", "application/json")
            }
        }.body()
    }

    override suspend fun updateShoppingList(dto: ShoppingListDto): UpdateResponse {
        return httpClient.patch(url) {
            setBody(dto)
            headers {
                append("Content-Type", "application/json")
            }
        }.body()
    }

    override suspend fun deleteShoppingList(request: DeleteShoppingListRequest): UpdateResponse {
        return httpClient.delete(url) {
            setBody(request)
            headers {
                append("Content-Type", "application/json")
            }
        }.body()
    }
}