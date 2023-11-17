package com.finitas.domain.ports

import com.finitas.domain.model.ReceiptBinaryData
import com.finitas.domain.model.ReceiptParseResult

interface ReceiptRepository {
    suspend fun parseReceipt(receipt: ReceiptBinaryData): ReceiptParseResult
}