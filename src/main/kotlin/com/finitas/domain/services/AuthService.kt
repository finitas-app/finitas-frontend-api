package com.finitas.domain.services

import com.finitas.config.exceptions.ErrorCode
import com.finitas.config.exceptions.InternalServerException
import com.finitas.domain.dto.store.UserDto
import com.finitas.domain.model.AuthUserRequest
import com.finitas.domain.model.CreateUserRequest
import com.finitas.domain.model.CreateUserResponse
import com.finitas.domain.ports.AuthRepository
import com.finitas.domain.ports.UserStoreRepository

class AuthService(
    private val authRepository: AuthRepository,
    private val userStoreRepository: UserStoreRepository,
) {
    suspend fun login(request: AuthUserRequest) = authRepository.loginUser(request)
    suspend fun signup(request: CreateUserRequest): CreateUserResponse {
        val createUserResponse = authRepository.createUser(request)
        try {
            userStoreRepository.insertUser(
                UserDto(
                    idUser = createUserResponse.userId,
                    visibleName = request.email.split("@")[0],
                    regularSpendings = arrayListOf(),
                    categories = arrayListOf(),
                    version = -1,
                )
            )
        } catch (error: Exception) {
            authRepository.deleteUser(createUserResponse.userId)
            throw InternalServerException(cause = error, errorCode = ErrorCode.CREATE_USER_ERROR)
        }
        return createUserResponse
    }
}