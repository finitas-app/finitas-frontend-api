package com.finitas

import com.finitas.config.serialization.configureSerialization
import com.finitas.config.di.configureDependencyInjection
import com.finitas.config.exceptions.configureExceptions
import com.finitas.config.web.configureAuth
import com.finitas.config.web.configureCORS
import com.finitas.config.web.configureWebsocket
import com.finitas.domain.api.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    System.setProperty("io.ktor.development", "true")
    embeddedServer(Netty, port = 8080, host = "192.168.0.156", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureDependencyInjection()
    configureSerialization()
    configureCORS()
    configureAuth()
    configureWebsocket()
    configureRouting()
    configureExceptions()
}