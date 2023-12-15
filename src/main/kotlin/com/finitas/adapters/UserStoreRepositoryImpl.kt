package com.finitas.adapters

import com.finitas.config.httpClient
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.dto.store.*
import com.finitas.domain.ports.UserStoreRepository
import io.ktor.client.call.*
import io.ktor.client.request.*

class UserStoreRepositoryImpl(urlProvider: UrlProvider) : UserStoreRepository {

    private val url = "${urlProvider.STORE_HOST_URL}/users"

    override suspend fun addRegularSpendings(
        idUser: String,
        regularSpendings: List<RegularSpendingDto>
    ): ResponseMessage {
        return httpClient.post("$url/$idUser/regular-spendings") {
            setBody(regularSpendings)
            headers {
                append("Content-Type", "application/json")
            }
        }.body()
    }

    override suspend fun upsertUser(dto: UserDto): ResponseMessage {
        return httpClient.put(url) {
            headers {
                append("Content-Type", "application/json")
            }
            setBody(dto)
        }.body()
    }

    override suspend fun getNicknames(request: GetVisibleNamesRequest): List<IdUserWithVisibleName> {
        return httpClient.get("$url/nicknames") {
            headers {
                append("Content-Type", "application/json")
            }
            setBody(request)
        }.body()
    }

    override suspend fun getRegularSpendings(idUser: String): List<RegularSpendingDto> {
        return httpClient.get("$url/$idUser/regular-spendings") {
            headers {
                append("Content-Type", "application/json")
            }
        }.body()
    }
}