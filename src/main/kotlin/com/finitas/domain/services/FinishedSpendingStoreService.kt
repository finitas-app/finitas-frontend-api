package com.finitas.domain.services

import com.finitas.domain.dto.store.*
import com.finitas.domain.ports.*
import java.util.*

class FinishedSpendingStoreService(
    private val repository: FinishedSpendingStoreRepository,
    private val reachableUsersRepository: ReachableUsersRepository,
    private val userNotifierPort: UserNotifierPort,
) {
    @Deprecated("To remove")
    suspend fun synchronizeFinishedSpendings(request: SynchronizationRequest<FinishedSpendingDto>) =
        repository.synchronizeFinishedSpendings(request)

    suspend fun updateWithChangedItems(request: List<FinishedSpendingDto>, petitioner: UUID) {
        repository.updateWithChangedItems(
            IdUserWithEntities(
                idUser = petitioner,
                changedValues = request
            )
        )
        val reachableUsers = reachableUsersRepository
            .getReachableUsersForUser(petitioner, null)
            .reachableUsers
        userNotifierPort.notifyUser(
            UserNotificationDto(
                event = UserNotificationEvent.FINISHED_SPENDING_CHANGED,
                targetUsers = listOf(
                    TargetUsersNotificationDto(
                        reachableUsers,
                        null,
                    )
                )
            )
        )
    }


    suspend fun getAllFinishedSpendings(idUser: UUID) = repository.getAllFinishedSpendings(idUser)
    suspend fun createFinishedSpending(requester: UUID, dto: FinishedSpendingDto) {
        //TODO: verify requester
        //TODO: use response
        repository.createFinishedSpending(dto)


        val reachableUsers = reachableUsersRepository
            .getReachableUsersForUser(dto.idUser, null)
            .reachableUsers
        userNotifierPort.notifyUser(
            UserNotificationDto(
                event = UserNotificationEvent.FINISHED_SPENDING_CHANGED,
                targetUsers = listOf(
                    TargetUsersNotificationDto(
                        reachableUsers,
                        null,
                    )
                )
            )
        )
    }


    suspend fun updateFinishedSpending(requester: UUID, dto: FinishedSpendingDto) {
        //TODO: verify requester
        //TODO: use response
        repository.updateFinishedSpending(dto)


        val reachableUsers = reachableUsersRepository
            .getReachableUsersForUser(dto.idUser, null)
            .reachableUsers
        userNotifierPort.notifyUser(
            UserNotificationDto(
                event = UserNotificationEvent.FINISHED_SPENDING_CHANGED,
                targetUsers = listOf(
                    TargetUsersNotificationDto(
                        reachableUsers,
                        null,
                    )
                )
            )
        )
    }


    suspend fun deleteFinishedSpending(requester: UUID, request: DeleteFinishedSpendingRequest) {
        //TODO: verify requester
        //TODO: use response
        repository.deleteFinishedSpending(request)


        val reachableUsers = reachableUsersRepository
            .getReachableUsersForUser(request.idUser, null)
            .reachableUsers
        userNotifierPort.notifyUser(
            UserNotificationDto(
                event = UserNotificationEvent.FINISHED_SPENDING_CHANGED,
                targetUsers = listOf(
                    TargetUsersNotificationDto(
                        reachableUsers,
                        null,
                    )
                )
            )
        )
    }

    suspend fun fetchUsersUpdates(request: List<IdUserWithVersion>) = repository.fetchUsersUpdates(request)
}