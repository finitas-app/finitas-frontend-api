package com.finitas.domain.services

import com.finitas.domain.model.AuthUserRequest
import com.finitas.domain.model.CreateUserRequest
import com.finitas.domain.ports.AuthRepository

class AuthService(private val repository: AuthRepository) {
    suspend fun login(request: AuthUserRequest) = repository.loginUser(request)
    fun signup(request: CreateUserRequest) = repository.createUser(request)
}