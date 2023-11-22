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

class UnauthorizedException(
    message: String = "Unauthorized",
    errorCode: ErrorCode = ErrorCode.AUTH_ERROR,
    cause: Exception? = null,
) : BaseException(message, errorCode, cause, HttpStatusCode.Unauthorized)

class ConflictException(
    message: String = "Conflict",
    errorCode: ErrorCode,
    cause: Exception? = null,
) : BaseException(message, errorCode, cause, HttpStatusCode.Conflict)

class InternalServerException(
    message: String = "Internal Server Error",
    cause: Exception? = null,
    errorCode: ErrorCode = ErrorCode.GENERIC_ERROR,
) : BaseException(message, errorCode, cause)
