package com.finitas.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthUserRequest(val email: String, val password: String)

@Serializable
data class AuthUserResponse(val accessToken: String, val refreshToken: String, val expires: Int)

@Serializable
data class CreateUserRequest(val email: String, val password: String)

@Serializable
data class CreateUserResponse(val userId: String, val nickname: String)