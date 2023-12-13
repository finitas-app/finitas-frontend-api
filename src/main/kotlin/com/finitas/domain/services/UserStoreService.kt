package com.finitas.domain.services

import com.finitas.domain.dto.store.GetVisibleNamesRequest
import com.finitas.domain.dto.store.RegularSpendingDto
import com.finitas.domain.dto.store.UserDto
import com.finitas.domain.ports.UserStoreRepository

class UserStoreService(private val repository: UserStoreRepository) {
    suspend fun addRegularSpending(idUser: String, regularSpendings: List<RegularSpendingDto>) =
        repository.addRegularSpendings(idUser, regularSpendings)

    suspend fun upsertUser(dto: UserDto) = repository.upsertUser(dto)
    suspend fun getNicknames(request: GetVisibleNamesRequest) = repository.getNicknames(request)
    suspend fun getRegularSpendings(idUser: String) = repository.getRegularSpendings(idUser)
}