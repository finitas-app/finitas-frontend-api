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
    FILE_NOT_PROVIDED,
    INVALID_FILE_PROVIDED,
}