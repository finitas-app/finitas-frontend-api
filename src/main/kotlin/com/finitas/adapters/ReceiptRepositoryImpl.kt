package com.finitas.adapters

import com.finitas.config.client
import com.finitas.config.exceptions.InternalServerException
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.model.ReceiptBinaryData
import com.finitas.domain.model.ReceiptParseResult
import com.finitas.domain.ports.ReceiptRepository
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.content.*
import kotlinx.serialization.Serializable

class ReceiptRepositoryImpl(private val urlProvider: UrlProvider) : ReceiptRepository {

    override suspend fun parseReceipt(receipt: ReceiptBinaryData): ReceiptParseResult {
        return try {
            val res = client.post(urlProvider.RECEIPT_SERVICE_HOST_URL){
                setBody(receipt.toMultiPartFormDataContent())
            }
            if (res.status.value == 200)
                res.body()
            else
                throw InternalServerException("Receipt parsing ended up with error: ${res.body<ReceiptParseErrorResponse>()}")
        } catch (exception: Exception) {
            throw InternalServerException("Receipt parsing ended up with error", exception)
        }
    }
}

@Serializable
private data class ReceiptParseErrorResponse(
    val detail: String,
)
