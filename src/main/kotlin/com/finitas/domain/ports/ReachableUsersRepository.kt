package com.finitas.domain.ports

import com.finitas.config.serialization.SerializableUUID
import com.finitas.domain.dto.store.Authority
import kotlinx.serialization.Serializable
import java.util.*

interface ReachableUsersRepository {

    suspend fun getReachableUsersForUser(idUser: UUID, idRoom: UUID? = null): ReachableUsersDto

    suspend fun getUsersUnderAuthority(providedUser: UUID, authority: Authority): UsersUnderAuthorityResponse
}

@Serializable
data class UsersUnderAuthorityResponse(
    val users: Set<SerializableUUID>,
)
