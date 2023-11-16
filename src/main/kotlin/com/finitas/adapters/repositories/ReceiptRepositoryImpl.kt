package com.finitas.adapters.repositories

import com.finitas.config.Config
import com.finitas.config.Logger
import com.finitas.config.client
import com.finitas.domain.model.Receipt
import com.finitas.domain.model.ReceiptParseResult
import com.finitas.domain.ports.ReceiptRepository
import com.finitas.exceptions.InternalServerException
import io.ktor.client.call.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

class ReceiptRepositoryImpl: ReceiptRepository {
    override suspend fun parseReceipt(receipt: Receipt): ReceiptParseResult {
        return try {
            client.submitFormWithBinaryData(
                url = Config.RECEIPT_SERVICE_HOST_URL,
                formData {
                    append("receipt", receipt.file, Headers.build {
                        append(HttpHeaders.ContentType, "image/png")
                        append(HttpHeaders.ContentDisposition, "filename=\"receipt\"")
                    })
                },
            ).body<ReceiptParseResult>()
        } catch (exception: Exception)  {
            Logger.error("Receipt parsing ended up with error", exception)
            throw InternalServerException("Failed to parse receipt")
        }
    }
}