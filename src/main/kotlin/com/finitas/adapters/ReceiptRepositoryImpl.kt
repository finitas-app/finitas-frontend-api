package com.finitas.adapters

import com.finitas.config.httpClient
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.model.Base64Receipt
import com.finitas.domain.ports.ReceiptRepository
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*

class ReceiptRepositoryImpl(private val urlProvider: UrlProvider) : ReceiptRepository {
    override suspend fun parseReceipt(receipt: Base64Receipt): Map<String, String> {
        return httpClient
            .post(urlProvider.RECEIPT_SERVICE_HOST_URL) {
                setBody(receipt)
                timeout { requestTimeoutMillis = urlProvider.RECEIPT_PARSING_TIMEOUT }
            }
            .body()
    }
}
