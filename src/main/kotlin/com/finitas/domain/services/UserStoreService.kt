package com.finitas.domain.services

import com.finitas.domain.api.ChangeSpendingCategoryDto
import com.finitas.domain.api.SyncCategoriesRequest
import com.finitas.domain.api.SyncCategoriesResponse
import com.finitas.domain.dto.store.*
import com.finitas.domain.ports.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class UserStoreService(
    private val repository: UserStoreRepository,
    private val reachableUsersRepository: ReachableUsersRepository,
    private val notifierPort: UserNotifierPort,
) {
    suspend fun addRegularSpending(idUser: UUID, regularSpendings: List<RegularSpendingDto>) =
        repository.addRegularSpendings(idUser, regularSpendings)

    suspend fun getNicknames(requester: UUID, request: GetVisibleNamesRequest): List<IdUserWithVisibleName> {
        val reachableUsers = reachableUsersRepository
            .getReachableUsersForUser(requester, null)
            .reachableUsers
            .toSet()
        val toRetrieve =
            if (request.userIds.isEmpty()) {
                GetVisibleNamesRequest(
                    reachableUsers.map { UserIdValue(it) }
                )
            } else {
                request.copy(userIds = request.userIds.filter { it.userId in reachableUsers })
            }
        return repository.getNicknames(
            toRetrieve
        )
    }

    suspend fun getRegularSpendings(idUser: UUID) = repository.getRegularSpendings(idUser)
    suspend fun updateNickname(request: IdUserWithVisibleName) {
        val reachableUsers = reachableUsersRepository
            .getReachableUsersForUser(request.idUser, null)
            .reachableUsers
        repository.updateNickname(request)
        notifierPort.notifyUser(
            UserNotificationDto(
                event = UserNotificationEvent.USERNAME_CHANGE,
                targetUsers = listOf(
                    TargetUsersNotificationDto(
                        reachableUsers,
                        Json.encodeToString(UserIdValue(request.idUser)),
                    )
                )
            )
        )
    }

    suspend fun deleteRegularSpending(idUser: UUID, idSpendingSummary: UUID) =
        repository.deleteRegularSpending(idUser = idUser, idSpendingSummary = idSpendingSummary)

    suspend fun getUser(idUser: UUID) = repository.getUser(idUser)
    suspend fun getUsers(userIds: List<IdUserWithVersion>) = repository.getUsers(userIds)

    suspend fun addCategories(requester: UUID, changeSpendingCategoryDto: ChangeSpendingCategoryDto) {
        val reachableUsers = reachableUsersRepository
            .getReachableUsersForUser(requester)
            .reachableUsers
        repository.addCategories(requester, changeSpendingCategoryDto)
        notifierPort.notifyUser(
            UserNotificationDto(
                event = UserNotificationEvent.CATEGORY_CHANGED,
                targetUsers = listOf(
                    TargetUsersNotificationDto(
                        reachableUsers,
                        Json.encodeToString(UserIdValue(requester)),
                    )
                )
            )
        )
    }

    suspend fun syncCategories(requester: UUID, syncCategoriesRequest: SyncCategoriesRequest): SyncCategoriesResponse {
        val reachableUsers = reachableUsersRepository
            .getReachableUsersForUser(requester)
            .reachableUsers
            .toSet()
        // TODO: send unavailableUsers to client
        return SyncCategoriesResponse(
            repository
                .getCategoriesFromVersions(
                    syncCategoriesRequest
                        .copy(
                            userVersions = syncCategoriesRequest
                                .userVersions
                                .filter { it.idUser in reachableUsers }
                        )
                )
                .userCategories,
            listOf(),
        )
    }
}
