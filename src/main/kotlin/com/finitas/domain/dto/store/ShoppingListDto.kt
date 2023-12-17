package com.finitas.domain.dto.store

import com.finitas.config.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class DeleteShoppingListRequest(
    @Serializable(UUIDSerializer::class)
    val idShoppingList: UUID,
    @Serializable(UUIDSerializer::class)
    val idUser: UUID,
)

@Serializable
class ShoppingListDto(
    @Serializable(UUIDSerializer::class)
    val idShoppingList: UUID,
    val shoppingItems: List<ShoppingItemDto>,
    val version: Int,
    @Serializable(UUIDSerializer::class)
    val idUser: UUID,
    val isDeleted: Boolean,
)

@Serializable
data class ShoppingItemDto(
    @Serializable(UUIDSerializer::class)
    val idShoppingItem: UUID,
    val isDone: Int,
    val spendingRecordData: SpendingRecordDataDto,
)