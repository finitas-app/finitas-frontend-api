package com.finitas.domain.api

import com.finitas.adapters.userNotifier
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.configureRouting() = routing {
    route("/api") {
        receiptRouting()
        authRouting()
        storeRouting()
        authenticate { roomRouter() }
        authenticate { userNotifier() }
    }
}