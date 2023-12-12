package com.finitas.adapters

import com.finitas.config.urls.UrlProvider
import com.finitas.domain.dto.store.*
import com.finitas.domain.ports.UserStoreRepository

class UserStoreRepositoryImpl(private val urlProvider: UrlProvider) : UserStoreRepository {
    override suspend fun addRegularSpendings(
        idUser: String,
        regularSpendings: List<RegularSpendingDto>
    ): ResponseMessage {
        TODO("Not yet implemented")
    }

    override suspend fun upsertUser(dto: UserDto): ResponseMessage {
        TODO("Not yet implemented")
    }

    override suspend fun getNicknames(request: GetVisibleNamesRequest): List<IdUserWithVisibleName> {
        TODO("Not yet implemented")
    }
}