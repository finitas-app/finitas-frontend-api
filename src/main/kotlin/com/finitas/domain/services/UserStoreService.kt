package com.finitas.domain.services

import com.finitas.domain.dto.store.GetVisibleNamesRequest
import com.finitas.domain.dto.store.IdUserWithVisibleName
import com.finitas.domain.dto.store.RegularSpendingDto
import com.finitas.domain.ports.UserStoreRepository
import java.util.*

class UserStoreService(private val repository: UserStoreRepository) {
    suspend fun addRegularSpending(idUser: UUID, regularSpendings: List<RegularSpendingDto>) =
        repository.addRegularSpendings(idUser, regularSpendings)

    suspend fun getNicknames(request: GetVisibleNamesRequest) = repository.getNicknames(request)
    suspend fun getRegularSpendings(idUser: UUID) = repository.getRegularSpendings(idUser)
    suspend fun updateNickname(request: IdUserWithVisibleName) = repository.updateNickname(request)
    suspend fun deleteRegularSpending(idUser: UUID, idRegularSpending: UUID) =
        repository.deleteRegularSpending(idUser = idUser, idRegularSpending = idRegularSpending)
}