package com.finitas.domain.services

import com.finitas.domain.dto.store.DeleteShoppingListRequest
import com.finitas.domain.dto.store.ShoppingListDto
import com.finitas.domain.dto.store.SynchronizationRequest
import com.finitas.domain.ports.ShoppingListStoreRepository
import java.util.*

class ShoppingListStoreService(
    private val shoppingListStoreRepository: ShoppingListStoreRepository
) {
    suspend fun synchronizeShoppingLists(request: SynchronizationRequest<ShoppingListDto>) =
        shoppingListStoreRepository.synchronizeShoppingLists(request)

    suspend fun getAllShoppingLists(idUser: UUID) =
        shoppingListStoreRepository.getAllShoppingLists(idUser)

    suspend fun createShoppingList(dto: ShoppingListDto) =
        shoppingListStoreRepository.createShoppingList(dto)

    suspend fun updateShoppingList(dto: ShoppingListDto) =
        shoppingListStoreRepository.updateShoppingList(dto)

    suspend fun deleteShoppingList(request: DeleteShoppingListRequest) =
        shoppingListStoreRepository.deleteShoppingList(request)
}