package com.finitas.adapters

import com.finitas.config.client
import com.finitas.config.exceptions.InternalServerException
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.model.Receipt
import com.finitas.domain.model.ReceiptParseResult
import com.finitas.domain.ports.ReceiptRepository
import io.ktor.client.call.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

class ReceiptRepositoryImpl(private val urlProvider: UrlProvider) : ReceiptRepository {
    override suspend fun parseReceipt(receipt: Receipt): ReceiptParseResult {
        return try {
            client.submitFormWithBinaryData(
                url = urlProvider.RECEIPT_SERVICE_HOST_URL,
                formData {
                    append("receipt", receipt.file, Headers.build {
                        append(HttpHeaders.ContentType, "image/png")
                        append(HttpHeaders.ContentDisposition, "filename=\"receipt\"")
                    })
                },
            ).body<ReceiptParseResult>()
        } catch (exception: Exception) {
            throw InternalServerException("Receipt parsing ended up with error", exception)
        }
    }
}