package com.finitas.config.exceptions

import io.ktor.http.*

open class BaseException(
    message: String,
    val errorCode: ErrorCode,
    cause: Exception? = null,
    val statusCode: HttpStatusCode = HttpStatusCode.InternalServerError,
) : Exception(message, cause)

class NotFoundException(
    message: String = "Not found",
    errorCode: ErrorCode = ErrorCode.NOT_FOUND,
    cause: Exception? = null,
) : BaseException(message, errorCode, cause, HttpStatusCode.NotFound)

class BadRequestException(
    message: String = "Bad request",
    errorCode: ErrorCode,
    cause: Exception? = null,
) : BaseException(message, errorCode, cause, HttpStatusCode.BadRequest)

class InternalServerException(
    message: String = "Internal Server Error",
    cause: Exception? = null,
    errorCode: ErrorCode = ErrorCode.GENERIC_ERROR,
) : BaseException(message, errorCode, cause)
