package com.finitas.domain.ports

import com.finitas.domain.dto.store.*

interface UserStoreRepository {
    suspend fun addRegularSpendings(idUser: String, regularSpendings: List<RegularSpendingDto>): ResponseMessage
    suspend fun upsertUser(dto: UserDto): ResponseMessage
    suspend fun getNicknames(request: GetVisibleNamesRequest): List<IdUserWithVisibleName>
}