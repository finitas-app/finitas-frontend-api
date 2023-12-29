package com.finitas.config.web

import com.auth0.jwk.JwkProviderBuilder
import com.finitas.config.exceptions.ErrorCode
import com.finitas.config.exceptions.ErrorResponse
import com.finitas.config.exceptions.UnauthorizedException
import com.finitas.config.urls.UrlProvider
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.util.*
import org.koin.ktor.ext.inject
import java.util.*
import java.util.concurrent.TimeUnit

fun Application.configureAuth() {
    val urlProvider by inject<UrlProvider>()

    val jwkProvider = JwkProviderBuilder(urlProvider.AUTH0_DOMAIN)
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    install(Authentication) {
        jwt {
            verifier(jwkProvider, "${urlProvider.AUTH0_DOMAIN}/")
            validate { credential ->
                val containsAudience = credential.payload.audience.contains(urlProvider.AUTH0_FINITAS_API_AUDIENCE)
                val userId = try {
                    UUID.fromString(credential.payload.subject.split("|")[1])
                } catch (_: Exception) {
                    throw UnauthorizedException("User id is invalid", ErrorCode.ID_USER_INVALID)
                }
                this.attributes.put(AttributeKey("userId"), userId)
                if (containsAudience) JWTPrincipal(credential.payload)
                else null
            }
            challenge { _, _ ->
                call.respond(
                    HttpStatusCode.Unauthorized, ErrorResponse(
                        errorCode = ErrorCode.AUTH_ERROR,
                        errorMessage = "Token is not valid or has expired",
                    )
                )
            }
        }
    }
}