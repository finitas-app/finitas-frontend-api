package com.finitas.config.exceptions

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val errorCode: ErrorCode,
    val errorMessage: String? = null,
)

enum class ErrorCode {
    NOT_FOUND,
    GENERIC_ERROR,
    NO_FILE_PROVIDED,
    AUTH_ERROR,
    CONFIGURATION_ERROR,
}