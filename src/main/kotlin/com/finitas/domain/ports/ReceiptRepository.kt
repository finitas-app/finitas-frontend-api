package com.finitas.domain.ports

import com.finitas.domain.model.ReceiptBinaryData
import com.finitas.domain.model.ReceiptParseResult
import io.ktor.http.content.*

interface ReceiptRepository {
    suspend fun parseReceipt(receipt: ReceiptBinaryData): ReceiptParseResult
}