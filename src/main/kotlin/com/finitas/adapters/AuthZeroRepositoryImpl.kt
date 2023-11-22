package com.finitas.adapters

import com.finitas.config.Logger
import com.finitas.config.client
import com.finitas.config.exceptions.ConflictException
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
    val username: String? = null,
    val password: String? = null,
    val audience: String,
    val client_id: String,
    val client_secret: String,
    val grant_type: String,
    val scope: String? = null
)

@Serializable
data class SignupAuth0UserRequestBody(
    val email: String,
    val password: String,
    val connection: String,
)

@Serializable
data class Auth0TokenResponse(
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

@Serializable
data class SignupAuth0UserResponse(
    val user_id: String,
    val nickname: String,
) {
    fun toCreateUserResponse() = CreateUserResponse(
        userId = user_id,
        nickname = nickname
    )
}

class AuthZeroRepositoryImpl(private val urlProvider: UrlProvider) : AuthRepository {
    private val logger by Logger()
    private fun buildLoginAuth0UserRequest(request: AuthUserRequest) = LoginAuth0UserRequestBody(
        username = request.email,
        password = request.password,
        audience = urlProvider.AUTH0_FINITAS_API_AUDIENCE,
        client_id = urlProvider.AUTH0_CLIENT_ID,
        client_secret = urlProvider.AUTH0_CLIENT_SECRET,
        grant_type = "password",
        scope = "openid"
    )

    private fun buildAuthApiRequest() = LoginAuth0UserRequestBody(
        audience = "${urlProvider.AUTH0_DOMAIN}/api/v2/",
        client_id = urlProvider.AUTH0_CLIENT_ID,
        client_secret = urlProvider.AUTH0_CLIENT_SECRET,
        grant_type = "client_credentials",
    )

    private fun buildSignupAuth0UserRequest(request: CreateUserRequest) =
        SignupAuth0UserRequestBody(
            email = request.email,
            password = request.password,
            connection = "Username-Password-Authentication"
        )

    private suspend fun getTokenForManagementApi() =
        client
            .post("${urlProvider.AUTH0_DOMAIN}/oauth/token") {
                contentType(ContentType.Application.Json)
                buildAuthApiRequest().apply { setBody(this) }
            }.body<Auth0TokenResponse>()
            .access_token

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
            return response.body<Auth0TokenResponse>().toAuthUserResponse()
        }

        logger.error("Error response: ${response.bodyAsText()}")
        logger.error("HTTP code: ${response.status}")

        throw when (response.status) {
            HttpStatusCode.Forbidden -> UnauthorizedException(message = "Login failed")
            else -> InternalServerException(message = "Failed to login user", errorCode = ErrorCode.AUTH_ERROR)
        }
    }

    override suspend fun createUser(request: CreateUserRequest): CreateUserResponse {
        val response: HttpResponse
        val apiToken = getTokenForManagementApi()

        try {
            response = client.post("${urlProvider.AUTH0_DOMAIN}/api/v2/users") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Authorization", "Bearer $apiToken")
                }
                buildSignupAuth0UserRequest(request).apply { setBody(this) }
            }
        } catch (exception: Exception) {
            throw InternalServerException("Failed to create user", exception, ErrorCode.AUTH_ERROR)
        }

        if (response.status == HttpStatusCode.Created) {
            return response.body<SignupAuth0UserResponse>().toCreateUserResponse()
        }

        logger.error("Error response: ${response.bodyAsText()}")
        logger.error("HTTP code: ${response.status}")

        throw when (response.status) {
            HttpStatusCode.Forbidden -> UnauthorizedException(message = "Login failed")
            HttpStatusCode.Conflict -> ConflictException(message = "User already exists", errorCode = ErrorCode.AUTH_ERROR)
            else -> InternalServerException(message = "Failed to create user", errorCode = ErrorCode.AUTH_ERROR)
        }
    }
}