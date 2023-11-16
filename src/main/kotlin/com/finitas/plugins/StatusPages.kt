package com.finitas.plugins

import com.finitas.config.handleExceptions
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*

fun Application.configureExceptions() {
    install(StatusPages) { handleExceptions() }
}
