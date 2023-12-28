package com.finitas.domain.ports

import com.finitas.domain.model.AuthUserRequest
import com.finitas.domain.model.AuthUserResponse
import com.finitas.domain.model.CreateUserRequest
import com.finitas.domain.model.CreateUserResponse
import java.util.*

interface AuthRepository {
    suspend fun loginUser(request: AuthUserRequest): AuthUserResponse
    suspend fun createUser(request: CreateUserRequest): CreateUserResponse
    suspend fun deleteUser(idUser: UUID)
}