package com.finitas.adapters

import com.finitas.config.httpClient
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.ports.ReachableUsersDto
import com.finitas.domain.ports.ReachableUsersRepository
import io.ktor.client.call.*
import io.ktor.client.request.*
import java.util.*

class ReachableUsersRepositoryImpl(
    private val urlProvider: UrlProvider,
): ReachableUsersRepository {
    override suspend fun getReachableUsersForUser(idUser: UUID, idRoom: UUID?): ReachableUsersDto {
        val response: ReachableUsersDto = httpClient.get("${urlProvider.ROOM_MANAGER_HOST_URL}/rooms/users") {
            parameter("idUser", idUser)
            parameter("idRoom", idRoom)
        }.body()

        return response
    }
}