package com.finitas.adapters

import com.finitas.config.urls.UrlProvider
import com.finitas.domain.dto.store.*
import com.finitas.domain.ports.ShoppingListStoreRepository

class ShoppingListStoreRepositoryImpl(private val urlProvider: UrlProvider) : ShoppingListStoreRepository {
    override suspend fun synchronizeShoppingLists(request: SynchronizationRequest<ShoppingListDto>): SynchronizationResponse<ShoppingListDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllShoppingLists(idUser: String): List<ShoppingListDto> {
        TODO("Not yet implemented")
    }

    override suspend fun createShoppingList(dto: ShoppingListDto): UpdateResponse {
        TODO("Not yet implemented")
    }

    override suspend fun updateShoppingList(dto: ShoppingListDto): UpdateResponse {
        TODO("Not yet implemented")
    }

    override suspend fun deleteShoppingList(dto: DeleteShoppingListRequest): UpdateResponse {
        TODO("Not yet implemented")
    }

}