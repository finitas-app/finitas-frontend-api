package com.finitas.domain.api

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() = routing {
    route("/api") {
        receiptRouting()
        authRouting()
        storeRouting()
    }
}