package com.finitas.config.di

import com.finitas.config.urls.Profile
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDependencyInjection() {
    val profile = System.getProperty("profile") ?: "dev"
    println("Profile: $profile")
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
