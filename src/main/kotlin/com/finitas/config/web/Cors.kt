package com.finitas.config.web

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureCORS() {
    install(CORS) {
        anyHost()
        HttpMethod.DefaultMethods.forEach(this::allowMethod)
        allowHeader("authorization")
        allowCredentials = true
        allowNonSimpleContentTypes = true
    }
}