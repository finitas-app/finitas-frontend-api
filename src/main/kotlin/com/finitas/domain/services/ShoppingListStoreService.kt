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

    suspend fun createShoppingList(requester: UUID, dto: ShoppingListDto) {

        //TODO: verify requester
        //TODO: use response
        val response = shoppingListStoreRepository.createShoppingList(dto)

        val reachableUsers = reachableUsersRepository
            .getReachableUsersForUser(dto.idUser, null)
            .reachableUsers
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


    suspend fun updateShoppingList(requester: UUID, dto: ShoppingListDto) {
        //TODO: verify requester
        //TODO: use response
        val response = shoppingListStoreRepository.updateShoppingList(dto)

        val reachableUsers = reachableUsersRepository
            .getReachableUsersForUser(dto.idUser, null)
            .reachableUsers
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

    suspend fun deleteShoppingList(requester: UUID, request: DeleteShoppingListRequest) {
        //TODO: verify requester
        //TODO: use response
        val response =shoppingListStoreRepository.deleteShoppingList(request)

        val reachableUsers = reachableUsersRepository
            .getReachableUsersForUser(request.idUser, null)
            .reachableUsers
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

    suspend fun updateWithChangedItems(request: List<ShoppingListDto>, petitioner: UUID) {
        //TODO: verify requester
        val reachableUsers = reachableUsersRepository
            .getReachableUsersForUser(petitioner, null)
            .reachableUsers
        shoppingListStoreRepository.updateWithChangedItems(
            IdUserWithEntities(
                idUser = petitioner,
                changedValues = request
            )
        )
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


    suspend fun fetchUsersUpdates(request: List<IdUserWithVersion>) = shoppingListStoreRepository.fetchUsersUpdates(request)
}