package com.finitas.config

import com.finitas.exceptions.BaseException
import io.ktor.http.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

@Serializable
internal data class ResponseMessage(val message: String)

fun StatusPagesConfig.handleExceptions() {
    exception<Throwable> { call, throwable ->
        if(throwable is BaseException) {
            Logger.error("Base exception", throwable.cause)
            call.respond(
                throwable.statusCode,
                ResponseMessage(throwable.message)
            )
        }
        else {
            Logger.error("Unknown error", throwable)
            call.respond(
                HttpStatusCode.InternalServerError,
                ResponseMessage("Internal Server Error")
            )
        }
    }
}