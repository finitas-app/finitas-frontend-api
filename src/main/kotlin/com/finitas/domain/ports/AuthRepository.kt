package com.finitas.domain.ports

import com.finitas.domain.model.AuthUserRequest
import com.finitas.domain.model.AuthUserResponse
import com.finitas.domain.model.CreateUserRequest
import com.finitas.domain.model.CreateUserResponse

interface AuthRepository {
    suspend fun loginUser(request: AuthUserRequest): AuthUserResponse
    fun createUser(request: CreateUserRequest): CreateUserResponse
}