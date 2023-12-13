package com.finitas.adapters

import com.finitas.config.Logger
import com.finitas.config.UUIDSerializer
import com.finitas.config.exceptions.*
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
import java.time.LocalDateTime
import java.util.*

private val auth0lHttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
            }
        )
    }
}

private class Token(
    val value: String,
    val expiry: LocalDateTime,
)

private class ManagementApiToken(private val urlProvider: UrlProvider) {
    private val logger by Logger()
    private var token: Token? = null
    private val mutex = Mutex()
    suspend fun get(): Token {
        if (token == null || token!!.expiry < LocalDateTime.now()) {
            mutex.withLock {
                token = token ?: generate()
            }
        }
        return token!!
    }

    suspend fun getWithRefresh(): String {
        mutex.withLock {
            token = generate()
        }
        return token!!
    }

    private fun buildAuthApiRequest() = LoginAuth0APIRequestBody(
        audience = "${urlProvider.AUTH0_DOMAIN}/api/v2/",
        clientId = urlProvider.AUTH0_CLIENT_ID,
        clientSecret = urlProvider.AUTH0_CLIENT_SECRET,
        grantType = "client_credentials",
    )

    private suspend fun generate(): Token {
        return try {
            auth0lHttpClient
                .post("${urlProvider.AUTH0_DOMAIN}/oauth/token") {
                    contentType(ContentType.Application.Json)
                    buildAuthApiRequest().apply { setBody(this) }
                }.body<Auth0TokenResponse>()
                .let {
                    Token(
                        value = it.accessToken,
                        expiry = LocalDateTime.now().plusSeconds(it.expiresIn.toLong())
                    )
                }
        } catch (cause: Exception) {
            logger.error("Failed to generate management API token.")
            throw InternalServerException(errorCode = ErrorCode.CONFIGURATION_ERROR, cause = cause)
        }
    }
}

class AuthZeroRepositoryImpl(private val urlProvider: UrlProvider) : AuthRepository {

    private val logger by Logger()
    private val managementApiToken = ManagementApiToken(urlProvider)
    private val auth0WeakPasswordMessage = "PasswordStrengthError: Password is too weak"
    private val auth0EmailValidationFailedMessageBeginning =
        "Payload validation error: 'Object didn't pass validation for format email:"
    private val auth0ApiTokenExpiredMessage = "Expired token received for JSON Web Token validation"

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

    private suspend fun sendCreateUserRequest(request: CreateUserRequest, apiToken: String): HttpResponse {
        val response: HttpResponse

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

        return response
    }

    private suspend fun isResponseFailedDueToApiTokenExpired(response: HttpResponse) =
        response.status == HttpStatusCode.Unauthorized
                && response.body<SignupAuth0UserErrorResponse>().message == auth0ApiTokenExpiredMessage

    override suspend fun createUser(request: CreateUserRequest): CreateUserResponse {
        var response = sendCreateUserRequest(request, managementApiToken.get())

        if (isResponseFailedDueToApiTokenExpired(response)) {
            logger.info("Refreshing API auth token.")
            response = sendCreateUserRequest(request, managementApiToken.getWithRefresh())
        }

        if (response.status == HttpStatusCode.Created) {
            return response.body<SignupAuth0UserResponse>().toCreateUserResponse()
        }

        val errorResponseBody = response.body<SignupAuth0UserErrorResponse>()

        logger.error("Error response: ${errorResponseBody.message}, HTTP code: ${response.status}")

        throw when (response.status) {
            HttpStatusCode.BadRequest -> handleBadRequestResponseWithBaseException(response)
            HttpStatusCode.Forbidden -> UnauthorizedException(message = "Login failed")
            HttpStatusCode.Conflict -> ConflictException(
                message = "User already exists",
                errorCode = ErrorCode.SIGN_UP_USER_EXISTS
            )

            else -> InternalServerException(message = "Failed to create user", errorCode = ErrorCode.AUTH_ERROR)
        }
    }

    private suspend fun handleBadRequestResponseWithBaseException(response: HttpResponse): BaseException {
        val body = response.body<SignupAuth0UserBadRequestResponse>()
        val statusCode =
            if (body.message == auth0WeakPasswordMessage) ErrorCode.SIGN_UP_PASSWORD_WEAK
            else if (body.message.startsWith(auth0EmailValidationFailedMessageBeginning)) ErrorCode.SIGN_UP_LOGIN_INVALID
            else ErrorCode.AUTH_ERROR

        return if (statusCode != ErrorCode.AUTH_ERROR) BadRequestException(
            body.message,
            statusCode
        ) else InternalServerException(message = "Failed to create user", errorCode = statusCode)
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

@Serializable
data class SignupAuth0UserErrorResponse(
    val message: String,
)

@Serializable
data class SignupAuth0UserBadRequestResponse(
    val message: String,
)