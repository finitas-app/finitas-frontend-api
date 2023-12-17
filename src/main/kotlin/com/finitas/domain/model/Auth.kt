package com.finitas.domain.model

import com.finitas.config.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class AuthUserRequest(val email: String, val password: String)

@Serializable
data class AuthUserResponse(val accessToken: String, val expires: Int, val idUser: String)

@Serializable
data class CreateUserRequest(val email: String, val password: String)

@Serializable
data class CreateUserResponse(@Serializable(with = UUIDSerializer::class) val userId: UUID, val nickname: String)