package com.finitas.config.di

import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection() {
    val isDevelopment = environment.developmentMode

    install(Koin) {
        slf4jLogger()
        modules(
            urlsModule(isDevelopment),
            receiptModule,
            authModule,
            storeModule
        )
    }
}
