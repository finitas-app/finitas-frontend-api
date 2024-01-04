package com.finitas.domain.ports

import com.finitas.domain.dto.store.*
import java.util.*

interface ShoppingListStoreRepository {
    suspend fun synchronizeShoppingLists(request: SynchronizationRequest<ShoppingListDto>): SynchronizationResponse<ShoppingListDto>
    suspend fun getAllShoppingLists(idUser: UUID): List<ShoppingListDto>
    suspend fun createShoppingList(dto: ShoppingListDto): UpdateResponse
    suspend fun updateShoppingList(dto: ShoppingListDto): UpdateResponse
    suspend fun deleteShoppingList(request: DeleteShoppingListRequest): UpdateResponse
    suspend fun updateWithChangedItems(idUserWithEntities: IdUserWithEntities<ShoppingListDto>)
    suspend fun fetchUsersUpdates(request: List<IdUserWithVersion>): List<FetchUpdatesResponse<ShoppingListDto>>
}