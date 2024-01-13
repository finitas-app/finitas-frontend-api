package com.finitas.domain.services

import com.finitas.config.exceptions.NotEnoughAuthorityToOperateOnUserException
import com.finitas.domain.dto.store.*
import com.finitas.domain.ports.*
import java.util.*

class FinishedSpendingStoreService(
    private val repository: FinishedSpendingStoreRepository,
    private val reachableUsersRepository: ReachableUsersRepository,
    private val userNotifierPort: UserNotifierPort,
) {
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


    suspend fun getAllFinishedSpendings(requester: UUID, idUser: UUID): List<FinishedSpendingDto> {
        val reachableUsersForDeletion = reachableUsersRepository.getUsersUnderAuthority(
            requester,
            authority = Authority.READ_USERS_DATA,
        ).users
        if (idUser !in reachableUsersForDeletion) {
            throw NotEnoughAuthorityToOperateOnUserException(idUser)
        }
        return repository.getAllFinishedSpendings(idUser)
    }
    suspend fun createFinishedSpending(requester: UUID, dto: FinishedSpendingDto) {
        val reachableUsersForDeletion = reachableUsersRepository.getUsersUnderAuthority(
            requester,
            authority = Authority.MODIFY_USERS_DATA,
        ).users
        if (dto.idUser !in reachableUsersForDeletion) {
            throw NotEnoughAuthorityToOperateOnUserException(dto.idUser)
        }
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
        val reachableUsersForDeletion = reachableUsersRepository.getUsersUnderAuthority(
            requester,
            authority = Authority.MODIFY_USERS_DATA,
        ).users
        if (dto.idUser !in reachableUsersForDeletion) {
            throw NotEnoughAuthorityToOperateOnUserException(dto.idUser)
        }
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
        val reachableUsersForDeletion = reachableUsersRepository.getUsersUnderAuthority(
            requester,
            authority = Authority.MODIFY_USERS_DATA,
        ).users
        if (request.idUser !in reachableUsersForDeletion) {
            throw NotEnoughAuthorityToOperateOnUserException(request.idUser)
        }
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

    suspend fun fetchUsersUpdates(requester: UUID, request: List<IdUserWithVersion>): List<FetchUpdatesResponse<FinishedSpendingDto>> {
        val reachableUsers = reachableUsersRepository.getUsersUnderAuthority(
            requester,
            authority = Authority.READ_USERS_DATA,
        ).users

        return repository.fetchUsersUpdates(request.filter { it.idUser in reachableUsers })
    }
}