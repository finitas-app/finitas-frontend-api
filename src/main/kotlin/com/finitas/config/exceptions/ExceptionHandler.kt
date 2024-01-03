package com.finitas.config.exceptions

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*


suspend fun Application.handleException(call: ApplicationCall, exception: Exception) = when (exception) {
    is BaseException -> {
        log.error("${exception::class.simpleName}: ${exception.message}", exception.cause)
        call.respond(
            exception.statusCode,
            ErrorResponse(
                exception.errorCode,
                exception.message
            )
        )
    }

    else -> {
        log.error("Unknown exception: ${exception.message}", exception)
        exception.printStackTrace()
        call.respond(
            HttpStatusCode.InternalServerError,
            ErrorResponse(
                ErrorCode.GENERIC_ERROR,
            )
        )
    }
}