package com.finitas.adapters

import com.finitas.config.contentTypeJson
import com.finitas.config.httpClient
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.dto.store.*
import com.finitas.domain.ports.UserStoreRepository
import io.ktor.client.call.*
import io.ktor.client.request.*
import java.util.*

class UserStoreRepositoryImpl(urlProvider: UrlProvider) : UserStoreRepository {

    private val url = "${urlProvider.STORE_HOST_URL}/users"

    override suspend fun addRegularSpendings(
        idUser: UUID,
        regularSpendings: List<RegularSpendingDto>
    ): ResponseMessage {
        return httpClient.post("$url/$idUser/regular-spendings") {
            setBody(regularSpendings)
            contentTypeJson()
        }.body()
    }

    override suspend fun insertUser(dto: UserDto) {
        httpClient.put(url) {
            setBody(dto)
            contentTypeJson()
        }
    }

    override suspend fun getNicknames(request: GetVisibleNamesRequest): List<IdUserWithVisibleName> {
        return httpClient.get("$url/nicknames") {
            setBody(request)
            contentTypeJson()
        }.body()
    }

    override suspend fun getRegularSpendings(idUser: UUID): List<RegularSpendingDto> {
        return httpClient.get("$url/$idUser/regular-spendings"){
            contentTypeJson()
        }.body()
    }

    override suspend fun updateNickname(request: IdUserWithVisibleName) {
        httpClient.patch("$url/nicknames") {
            setBody(request)
            contentTypeJson()
        }
    }

    override suspend fun deleteRegularSpending(idUser: UUID, idSpendingSummary: UUID) {
        httpClient.delete("$url/$idUser/regular-spendings/$idSpendingSummary")
    }
}