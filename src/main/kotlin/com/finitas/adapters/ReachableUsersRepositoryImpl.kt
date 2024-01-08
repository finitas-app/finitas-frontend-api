package com.finitas.adapters

import com.finitas.config.httpClient
import com.finitas.config.serialization.SerializableUUID
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.dto.store.Authority
import com.finitas.domain.ports.ReachableUsersDto
import com.finitas.domain.ports.ReachableUsersRepository
import com.finitas.domain.ports.UsersUnderAuthorityResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import java.util.*

class ReachableUsersRepositoryImpl(
    private val urlProvider: UrlProvider,
) : ReachableUsersRepository {
    override suspend fun getReachableUsersForUser(idUser: UUID, idRoom: UUID?): ReachableUsersDto {
        val response: ReachableUsersDto = httpClient.get("${urlProvider.ROOM_MANAGER_HOST_URL}/rooms/users") {
            parameter("idUser", idUser)
            parameter("idRoom", idRoom)
        }.body()

        return response
    }

    override suspend fun getUsersUnderAuthority(providedUser: UUID, authority: Authority): UsersUnderAuthorityResponse {
        val response: UsersUnderAuthorityResponse = httpClient.get("${urlProvider.ROOM_MANAGER_HOST_URL}/rooms/users-under-authority") {
            parameter("idUser", providedUser)
            parameter("authority", authority)
        }.body()

        return response
    }
}
