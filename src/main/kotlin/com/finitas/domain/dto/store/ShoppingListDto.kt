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
    val name: String,
    val color: Int,
    override val version: Int,
    override val idUser: SerializableUUID,
    override val isDeleted: Boolean,
    val isFinished: Boolean,
) : SynchronizableEntity

@Serializable
data class ShoppingItemDto(
    // TODO delete unnecessary id, left only `idSpendingRecordData`
    val idShoppingItem: SerializableUUID,
    val amount: Int,
    val idSpendingRecordData: SerializableUUID,
    val name: String,
    val idCategory: SerializableUUID,
)