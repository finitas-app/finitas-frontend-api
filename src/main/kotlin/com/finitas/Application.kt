package com.finitas

import com.finitas.config.di.configureDependencyInjection
import com.finitas.config.exceptions.configureExceptions
import com.finitas.config.serialization.configureSerialization
import com.finitas.config.web.configureAuth
import com.finitas.config.web.configureCORS
import com.finitas.config.web.configureRedis
import com.finitas.config.web.configureWebsocket
import com.finitas.domain.api.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureDependencyInjection()
    configureSerialization()
    configureCORS()
    configureAuth()
    configureWebsocket()
    configureRedis()
    configureRouting()
    configureExceptions()
}