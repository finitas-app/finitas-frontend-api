package com.finitas.adapters

import com.finitas.config.httpClient
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.model.ReceiptBinaryData
import com.finitas.domain.model.ReceiptParseResult
import com.finitas.domain.ports.ReceiptRepository
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*

class ReceiptRepositoryImpl(private val urlProvider: UrlProvider) : ReceiptRepository {

    override suspend fun parseReceipt(receipt: ReceiptBinaryData): ReceiptParseResult {
        return httpClient.post(urlProvider.RECEIPT_SERVICE_HOST_URL) {
            setBody(receipt.toMultiPartFormDataContent())
            timeout {
                requestTimeoutMillis = urlProvider.RECEIPT_PARSING_TIMEOUT
            }
        }.body()
    }
}
