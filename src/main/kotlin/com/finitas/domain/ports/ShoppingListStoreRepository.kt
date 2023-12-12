package com.finitas.domain.ports

import com.finitas.domain.dto.store.*

interface ShoppingListStoreRepository {
    suspend fun synchronizeShoppingLists(request: SynchronizationRequest<ShoppingListDto>): SynchronizationResponse<ShoppingListDto>
    suspend fun getAllShoppingLists(idUser: String): List<ShoppingListDto>
    suspend fun createShoppingList(dto: ShoppingListDto): UpdateResponse
    suspend fun updateShoppingList(dto: ShoppingListDto): UpdateResponse
    suspend fun deleteShoppingList(dto: DeleteShoppingListRequest): UpdateResponse
}