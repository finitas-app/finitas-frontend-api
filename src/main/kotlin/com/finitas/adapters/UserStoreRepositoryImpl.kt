package com.finitas.adapters

import com.finitas.config.contentTypeJson
import com.finitas.config.httpClient
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.api.ChangeSpendingCategoryDto
import com.finitas.domain.api.SyncCategoriesRequest
import com.finitas.domain.dto.store.*
import com.finitas.domain.ports.GetCategoriesFromVersionResponse
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
        return httpClient.get("$url/$idUser/regular-spendings") {
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

    override suspend fun getUser(idUser: UUID): UserDto {
        return httpClient.get("$url/$idUser") {
            contentTypeJson()
        }.body()
    }

    override suspend fun getUsers(userIds: List<IdUserWithVersion>): List<UserDto> {
        return httpClient.get(url) {
            setBody(userIds)
            contentTypeJson()
        }.body()
    }

    override suspend fun addCategories(requester: UUID, changeSpendingCategoryDto: ChangeSpendingCategoryDto) {
        httpClient.post("$url/$requester/categories") {
            setBody(changeSpendingCategoryDto)
        }
    }

    override suspend fun getCategoriesFromVersions(syncCategoriesRequest: SyncCategoriesRequest): GetCategoriesFromVersionResponse {
        return httpClient.post("$url/categories/sync") {
            setBody(syncCategoriesRequest)
        }.body()
    }
}