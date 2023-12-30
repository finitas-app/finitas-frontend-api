package com.finitas.domain.ports

import com.finitas.domain.dto.store.*
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
}