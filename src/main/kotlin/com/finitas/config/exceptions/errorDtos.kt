package com.finitas.config.exceptions

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val errorCode: ErrorCode,
    val errorMessage: String? = null,
)

enum class ErrorCode {
    GENERIC_ERROR,
    AUTH_ERROR,
    CONFIGURATION_ERROR,
    FILE_NOT_PROVIDED,
    INVALID_FILE_PROVIDED,
    SIGN_UP_LOGIN_INVALID,
    SIGN_UP_PASSWORD_WEAK,
    SIGN_UP_USER_EXISTS
}