package com.finitas.domain.ports

import com.finitas.domain.api.ChangeSpendingCategoryDto
import com.finitas.domain.api.SyncCategoriesRequest
import com.finitas.domain.api.SyncCategoriesResponse
import com.finitas.domain.api.UserWithCategoriesDto
import com.finitas.domain.dto.store.*
import kotlinx.serialization.Serializable
import java.util.*

interface UserStoreRepository {
    suspend fun addRegularSpendings(idUser: UUID, regularSpendings: List<RegularSpendingDto>): ResponseMessage
    suspend fun insertUser(dto: UserDto)
    suspend fun getNicknames(request: GetVisibleNamesRequest): List<IdUserWithVisibleName>
    suspend fun getRegularSpendings(idUser: UUID): List<RegularSpendingDto>
    suspend fun updateNickname(request: IdUserWithVisibleName)
    suspend fun deleteRegularSpending(idUser: UUID, idSpendingSummary: UUID)
    suspend fun getUser(idUser: UUID): UserDto
    suspend fun getUsers(userIds: List<IdUserWithVersion>): List<UserDto>
    suspend fun addCategories(requester: UUID, changeSpendingCategoryDto: ChangeSpendingCategoryDto)
    suspend fun getCategoriesFromVersions(syncCategoriesRequest: SyncCategoriesRequest): GetCategoriesFromVersionResponse
}

@Serializable
data class GetCategoriesFromVersionResponse(
    val userCategories: List<UserWithCategoriesDto>,
)

