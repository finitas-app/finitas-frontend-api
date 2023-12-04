package com.finitas.domain.api

import com.finitas.domain.model.AuthUserRequest
import com.finitas.domain.model.CreateUserRequest
import com.finitas.domain.services.AuthService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

@Serializable
data class Response(val message: String, val userId: String)

fun Route.authRouting() {
    val service by inject<AuthService>()

    route("/auth") {
        post("/login") {
            call.receive<AuthUserRequest>()
                .let { service.login(it) }
                .let { call.respond(it) }
        }
        post("/signup") {
            call.receive<CreateUserRequest>()
                .let { service.signup(it) }
                .let { call.respond(it) }
        }
        authenticate {
            get("/test") {
                call.respond(Response("success", call.attributes[AttributeKey("userId")]))
            }
        }
    }
}
