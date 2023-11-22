package com.finitas.adapters

import com.finitas.config.Logger
import com.finitas.config.client
import com.finitas.config.exceptions.ErrorCode
import com.finitas.config.exceptions.InternalServerException
import com.finitas.config.exceptions.UnauthorizedException
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.model.AuthUserRequest
import com.finitas.domain.model.AuthUserResponse
import com.finitas.domain.model.CreateUserRequest
import com.finitas.domain.model.CreateUserResponse
import com.finitas.domain.ports.AuthRepository
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class LoginAuth0UserRequestBody(
    val username: String,
    val password: String,
    val audience: String,
    val client_id: String,
    val client_secret: String,
    val grant_type: String,
    val scope: String
)

@Serializable
data class LoginAuth0Response(
    val access_token: String,
    val expires_in: Int
) {
    fun toAuthUserResponse() =
        AuthUserResponse(
            accessToken = access_token,
            // todo: configure refresh token
            refreshToken = "",
            expires = expires_in
        )
}

class AuthZeroRepositoryImpl(private val urlProvider: UrlProvider) : AuthRepository {
    private val logger by Logger()
    private fun buildLoginAuth0UserRequest(request: AuthUserRequest): LoginAuth0UserRequestBody {
        return LoginAuth0UserRequestBody(
            username = request.email,
            password = request.password,
            audience = urlProvider.AUTH0_FINITAS_API_AUDIENCE,
            client_id = urlProvider.AUTH0_CLIENT_ID,
            client_secret = urlProvider.AUTH0_CLIENT_SECRET,
            grant_type = "password",
            scope = "openid"
        )
    }

    override suspend fun loginUser(request: AuthUserRequest): AuthUserResponse {
        val response: HttpResponse

        try {
            response = client.post("${urlProvider.AUTH0_DOMAIN}/oauth/token") {
                contentType(ContentType.Application.Json)
                buildLoginAuth0UserRequest(request).apply { setBody(this) }
            }
        } catch (exception: Exception) {
            throw InternalServerException("Failed to login user", exception, ErrorCode.AUTH_ERROR)
        }

        if (response.status == HttpStatusCode.OK) {
            return response.body<LoginAuth0Response>().toAuthUserResponse()
        }

        logger.error(response.body())
        throw when(response.status) {
            HttpStatusCode.Forbidden -> UnauthorizedException(message = "Login failed")
            else -> InternalServerException(message = "Failed to login user", errorCode = ErrorCode.AUTH_ERROR)
        }
    }

    override fun createUser(request: CreateUserRequest): CreateUserResponse {
        TODO("Not yet implemented")
    }
}