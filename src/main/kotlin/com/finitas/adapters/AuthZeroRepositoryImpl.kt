package com.finitas.adapters

import com.finitas.config.Logger
import com.finitas.config.UUIDSerializer
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
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.UUID

private val auth0lHttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
            }
        )
    }
}

private class ManagementApiToken(private val urlProvider: UrlProvider) {
    private val logger by Logger()
    private var token: String? = null
    private val mutex = Mutex()
    suspend fun get(): String {
        if (token == null) {
            mutex.withLock {
                token = token ?: generate()
            }
        }
        return token!!
    }

    private fun buildAuthApiRequest() = LoginAuth0APIRequestBody(
        audience = "${urlProvider.AUTH0_DOMAIN}/api/v2/",
        clientId = urlProvider.AUTH0_CLIENT_ID,
        clientSecret = urlProvider.AUTH0_CLIENT_SECRET,
        grantType = "client_credentials",
    )

    private suspend fun generate(): String {
        return try {
            auth0lHttpClient
                .post("${urlProvider.AUTH0_DOMAIN}/oauth/token") {
                    contentType(ContentType.Application.Json)
                    buildAuthApiRequest().apply { setBody(this) }
                }.body<Auth0TokenResponse>()
                .accessToken
        } catch (cause: Exception) {
            logger.error("Failed to generate management API token.")
            throw InternalServerException(errorCode = ErrorCode.CONFIGURATION_ERROR, cause = cause)
        }
    }
}

class AuthZeroRepositoryImpl(private val urlProvider: UrlProvider) : AuthRepository {

    private val logger by Logger()
    private val managementApiToken = ManagementApiToken(urlProvider)

    private fun buildLoginAuth0UserRequest(request: AuthUserRequest) = LoginAuth0UserRequestBody(
        username = request.email,
        password = request.password,
        audience = urlProvider.AUTH0_FINITAS_API_AUDIENCE,
        clientId = urlProvider.AUTH0_CLIENT_ID,
        clientSecret = urlProvider.AUTH0_CLIENT_SECRET,
        grantType = "password",
        scope = "openid"
    )

    private fun buildSignupAuth0UserRequest(request: CreateUserRequest) =
        SignupAuth0UserRequestBody(
            email = request.email,
            password = request.password,
            connection = "Username-Password-Authentication",
            userId = UUID.randomUUID()
        )

    override suspend fun loginUser(request: AuthUserRequest): AuthUserResponse {
        val response: HttpResponse

        try {
            response = auth0lHttpClient.post("${urlProvider.AUTH0_DOMAIN}/oauth/token") {
                contentType(ContentType.Application.Json)
                buildLoginAuth0UserRequest(request).apply { setBody(this) }
            }
        } catch (exception: Exception) {
            throw InternalServerException("Failed to login user", exception, ErrorCode.AUTH_ERROR)
        }

        if (response.status == HttpStatusCode.OK) {
            return response.body<Auth0TokenResponse>().toAuthUserResponse()
        }

        logger.error("Error response: ${response.bodyAsText()}, HTTP code: ${response.status}")

        throw when (response.status) {
            HttpStatusCode.Forbidden -> UnauthorizedException(message = "Login failed")
            else -> InternalServerException(message = "Failed to login user", errorCode = ErrorCode.AUTH_ERROR)
        }
    }

    override suspend fun createUser(request: CreateUserRequest): CreateUserResponse {
        val response: HttpResponse
        val apiToken = managementApiToken.get()

        try {
            response = auth0lHttpClient.post("${urlProvider.AUTH0_DOMAIN}/api/v2/users") {
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

        logger.error("Error response: ${response.bodyAsText()}, HTTP code: ${response.status}")

        throw when (response.status) {
            HttpStatusCode.Forbidden -> UnauthorizedException(message = "Login failed")
            HttpStatusCode.Conflict -> ConflictException(
                message = "User already exists",
                errorCode = ErrorCode.AUTH_ERROR
            )

            else -> InternalServerException(message = "Failed to create user", errorCode = ErrorCode.AUTH_ERROR)
        }
    }
}

@Serializable
data class LoginAuth0UserRequestBody(
    val username: String,
    val password: String,
    val audience: String,
    @SerialName("client_id")
    val clientId: String,
    @SerialName("client_secret")
    val clientSecret: String,
    @SerialName("grant_type")
    val grantType: String,
    val scope: String? = null
)

@Serializable
data class LoginAuth0APIRequestBody(
    val audience: String,
    @SerialName("client_id")
    val clientId: String,
    @SerialName("client_secret")
    val clientSecret: String,
    @SerialName("grant_type")
    val grantType: String,
    val scope: String? = null
)

@Serializable
data class SignupAuth0UserRequestBody(
    val email: String,
    val password: String,
    val connection: String,
    @Serializable(with = UUIDSerializer::class)
    @SerialName("user_id")
    val userId: UUID,
)

@Serializable
data class Auth0TokenResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_in")
    val expiresIn: Int
) {
    fun toAuthUserResponse() =
        AuthUserResponse(
            accessToken = accessToken,
            expires = expiresIn
        )
}

@Serializable
data class SignupAuth0UserResponse(
    @SerialName("user_id")
    val userId: String,
    val nickname: String,
) {
    fun toCreateUserResponse() = CreateUserResponse(
        userId = UUID.fromString(userId.split("|")[1]),
        nickname = nickname
    )
}