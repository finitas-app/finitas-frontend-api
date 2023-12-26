package com.finitas.config.di

import com.finitas.config.urls.Profile
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("ConfigureDependencyInjection")

fun Application.configureDependencyInjection() {
    val profile = System.getProperty("profile") ?: "dev"
    logger.info("Profile: $profile")
    install(Koin) {
        slf4jLogger()
        modules(
            urlsModule(Profile.valueOf(profile)),
            notificationModule,
            receiptModule,
            authModule,
            storeModule,
            roomModule,
        )
    }
}
