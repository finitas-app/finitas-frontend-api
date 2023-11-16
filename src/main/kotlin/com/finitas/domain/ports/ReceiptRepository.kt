package com.finitas.domain.ports

import com.finitas.domain.model.Receipt
import com.finitas.domain.model.ReceiptParseResult

interface ReceiptRepository {
    suspend fun parseReceipt(receipt: Receipt): ReceiptParseResult
}