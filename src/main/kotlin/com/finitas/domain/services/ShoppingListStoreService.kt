package com.finitas.domain.services

import com.finitas.domain.dto.store.DeleteShoppingListRequest
import com.finitas.domain.dto.store.ShoppingListDto
import com.finitas.domain.dto.store.SynchronizationRequest
import com.finitas.domain.ports.ShoppingListStoreRepository
import com.finitas.domain.ports.UserRoleRepository

class ShoppingListStoreService(
    private val shoppingListStoreRepository: ShoppingListStoreRepository
) {
    suspend fun synchronizeShoppingLists(request: SynchronizationRequest<ShoppingListDto>) =
        shoppingListStoreRepository.synchronizeShoppingLists(request)

    suspend fun getAllShoppingLists(idUser: String) =
        shoppingListStoreRepository.getAllShoppingLists(idUser)

    suspend fun createShoppingList(dto: ShoppingListDto) =
        shoppingListStoreRepository.createShoppingList(dto)

    suspend fun updateShoppingList(dto: ShoppingListDto) =
        shoppingListStoreRepository.updateShoppingList(dto)

    suspend fun deleteShoppingList(dto: DeleteShoppingListRequest) =
        shoppingListStoreRepository.deleteShoppingList(dto)
}