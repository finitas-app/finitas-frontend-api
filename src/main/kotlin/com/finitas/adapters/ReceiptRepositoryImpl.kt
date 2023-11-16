package com.finitas.adapters

import com.finitas.config.Logger
import com.finitas.config.client
import com.finitas.config.exceptions.InternalServerException
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.model.Receipt
import com.finitas.domain.model.ReceiptParseErrorResult
import com.finitas.domain.model.ReceiptParseSuccessResult
import com.finitas.domain.ports.ReceiptRepository
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

class ReceiptRepositoryImpl(private val urlProvider: UrlProvider) : ReceiptRepository {
    private val logger by Logger()
    override suspend fun parseReceipt(receipt: Receipt): ReceiptParseSuccessResult {
        val result = try {
            client.post("${urlProvider.RECEIPT_SERVICE_HOST_URL}/parse") {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("file", receipt.file, Headers.build {
                                // todo: header is ignored. Exception: Content-Type header is required for multipart processing
                                append(HttpHeaders.ContentType, "image/png")
                                append(HttpHeaders.ContentDisposition, "filename=\"receipt.png\"")
                            })
                        },
                    )
                )
                timeout {
                    requestTimeoutMillis = urlProvider.RECEIPT_PARSING_TIMEOUT
                }
            }
        } catch (exception: Exception) {
            throw InternalServerException("Receipt parsing ended up with error", exception)
        }

        if (result.status == HttpStatusCode.OK) return result.body<ReceiptParseSuccessResult>()
        else {
            val errorResponse = result.body<ReceiptParseErrorResult>()
            logger.error("Response message - ${errorResponse.detail}; status code - ${result.status}")
            throw InternalServerException("Receipt parsing ended up with error")
        }
    }
}