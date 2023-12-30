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
    override val version: Int,
    override val idUser: SerializableUUID,
    override val isDeleted: Boolean,
) : SynchronizableEntity

@Serializable
data class ShoppingItemDto(
    val idShoppingItem: SerializableUUID,
    val isDone: Int,
    val spendingRecordData: SpendingRecordDataDto,
)