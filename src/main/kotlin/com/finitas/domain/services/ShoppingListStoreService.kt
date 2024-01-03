package com.finitas.domain.services

import com.finitas.domain.dto.store.*
import com.finitas.domain.ports.*
import java.util.*

class ShoppingListStoreService(
    private val shoppingListStoreRepository: ShoppingListStoreRepository,
    private val userNotifierPort: UserNotifierPort,
    private val reachableUsersRepository: ReachableUsersRepository,
) {
    suspend fun synchronizeShoppingLists(request: SynchronizationRequest<ShoppingListDto>): SynchronizationResponse<ShoppingListDto> {
        //TODO: verify requester
        val reachableUsers = reachableUsersRepository
            .getReachableUsersForUser(request.idUser, null)
            .reachableUsers

        val response = shoppingListStoreRepository.synchronizeShoppingLists(request)
        if (request.objects.isNotEmpty()) {
            userNotifierPort.notifyUser(
                UserNotificationDto(
                    event = UserNotificationEvent.SHOPPING_LIST_CHANGED,
                    targetUsers = listOf(
                        TargetUsersNotificationDto(
                            reachableUsers,
                            null,
                        )
                    )
                )
            )
        }
        return response
    }

    suspend fun getAllShoppingLists(idUser: UUID) =
        shoppingListStoreRepository.getAllShoppingLists(idUser)

    suspend fun createShoppingList(dto: ShoppingListDto) {
        //TODO: verify requester
        val reachableUsers = reachableUsersRepository
            .getReachableUsersForUser(dto.idUser, null)
            .reachableUsers

        //TODO: use response
        val response = shoppingListStoreRepository.createShoppingList(dto)
        userNotifierPort.notifyUser(
            UserNotificationDto(
                event = UserNotificationEvent.SHOPPING_LIST_CHANGED,
                targetUsers = listOf(
                    TargetUsersNotificationDto(
                        reachableUsers,
                        null,
                    )
                )
            )
        )
    }


    suspend fun updateShoppingList(dto: ShoppingListDto) {
        //TODO: verify requester
        val reachableUsers = reachableUsersRepository
            .getReachableUsersForUser(dto.idUser, null)
            .reachableUsers
        //TODO: use response
        val response = shoppingListStoreRepository.updateShoppingList(dto)
        userNotifierPort.notifyUser(
            UserNotificationDto(
                event = UserNotificationEvent.SHOPPING_LIST_CHANGED,
                targetUsers = listOf(
                    TargetUsersNotificationDto(
                        reachableUsers,
                        null,
                    )
                )
            )
        )
    }

    suspend fun deleteShoppingList(request: DeleteShoppingListRequest) =
        shoppingListStoreRepository.deleteShoppingList(request)

    suspend fun updateWithChangedItems(request: List<ShoppingListDto>, petitioner: UUID) =
        shoppingListStoreRepository.updateWithChangedItems(
            IdUserWithEntities(
                idUser = petitioner,
                changedValues = request
            )
        )

    suspend fun fetchUsersUpdates(request: List<IdUserWithVersion>) = shoppingListStoreRepository.fetchUsersUpdates(request)
}