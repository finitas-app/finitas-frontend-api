package com.finitas.exceptions

import io.ktor.http.*

open class BaseException(
    override val message: String,
    override val cause: Exception? = null,
    val statusCode: HttpStatusCode = HttpStatusCode.InternalServerError
) : Exception(message, cause)

class NotFoundException(
    override val message: String = "Not found",
    override val cause: Exception? = null,
): BaseException(message, cause, HttpStatusCode.NotFound)

class BadRequestException(
    override val message: String = "Bad request",
    override val cause: Exception? = null,
): BaseException(message, cause, HttpStatusCode.BadRequest)

class InternalServerException(
    override val message: String = "Internal Server Error",
    override val cause: Exception? = null,
): BaseException(message, cause)
