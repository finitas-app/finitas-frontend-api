package com.finitas.domain.ports

import com.finitas.domain.model.ReceiptParseResult
import io.ktor.http.content.*

interface ReceiptRepository {
    suspend fun parseReceipt(receipt: MultiPartData): ReceiptParseResult
}