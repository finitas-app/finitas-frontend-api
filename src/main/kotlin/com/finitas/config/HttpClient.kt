package com.finitas.config

import com.finitas.config.exceptions.ErrorResponse
import com.finitas.config.exceptions.ExternalErrorException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json


val httpClient: HttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
            }
        )
    }
    HttpResponseValidator {
        validateResponse { response ->
            val httpStatusCode = response.status
            if (httpStatusCode.value in 400..599) {
                val error = response.body<ErrorResponse>()
                throw ExternalErrorException(error, httpStatusCode)
            }
        }
    }
}
