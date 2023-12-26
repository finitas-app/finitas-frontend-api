package com.finitas.domain.dto.store

import com.finitas.config.serialization.SerializableUUID
import kotlinx.serialization.Serializable

@Serializable
data class DeleteShoppingListRequest(
    val idShoppingList: SerializableUUID,
    val idUser: SerializableUUID,
)

@Serializable
class ShoppingListDto(
    val idShoppingList: SerializableUUID,
    val shoppingItems: List<ShoppingItemDto>,
    val version: Int,
    val idUser: SerializableUUID,
    val isDeleted: Boolean,
)

@Serializable
data class ShoppingItemDto(
    val idShoppingItem: SerializableUUID,
    val isDone: Int,
    val spendingRecordData: SpendingRecordDataDto,
)