package com.finitas.domain.model

import com.finitas.config.serialization.SerializableUUID
import kotlinx.serialization.Serializable

@Serializable
data class AuthUserRequest(val email: String, val password: String)

@Serializable
data class AuthUserResponse(val accessToken: String, val expires: Int, val idUser: String)

@Serializable
data class CreateUserRequest(val email: String, val password: String)

@Serializable
data class CreateUserResponse(val userId: SerializableUUID)