package com.finitas.config.web

import com.auth0.jwk.JwkProviderBuilder
import com.finitas.config.urls.UrlProvider
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject
import java.util.concurrent.TimeUnit

fun Application.configureAuth() {
    val urlProvider by inject<UrlProvider>()

    val jwkProvider = JwkProviderBuilder(urlProvider.AUTH0_DOMAIN)
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    install(Authentication) {
        jwt {
            verifier(jwkProvider, urlProvider.AUTH0_DOMAIN)
            validate { credential ->
                val containsAudience = credential.payload.audience.contains(urlProvider.AUTH0_FINITAS_API_AUDIENCE)

                if(containsAudience) JWTPrincipal(credential.payload)
                else null
            }
        }
    }
}