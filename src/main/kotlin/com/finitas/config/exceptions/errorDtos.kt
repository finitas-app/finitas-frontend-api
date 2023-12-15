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
    ACTION_FORBIDDEN_ERROR,
    CONFIGURATION_ERROR,
    FILE_NOT_PROVIDED,
    INVALID_FILE_PROVIDED,
    ID_ROOM_NOT_PROVIDED,
    ID_USER_NOT_PROVIDED,
    FINISHED_SPENDING_NOT_FOUND,
    FINISHED_SPENDING_EXISTS,
    SHOPPING_LIST_NOT_FOUND,
    SHOPPING_LIST_EXISTS,
    USER_NOT_FOUND,
    STORE_REQUEST_INPUT_INVALID,
    SIGN_UP_LOGIN_INVALID,
    SIGN_UP_PASSWORD_WEAK,
    SIGN_UP_USER_EXISTS
}