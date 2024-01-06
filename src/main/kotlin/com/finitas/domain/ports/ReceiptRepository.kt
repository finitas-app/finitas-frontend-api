package com.finitas.domain.ports

import com.finitas.domain.model.Base64Receipt

interface ReceiptRepository {
    suspend fun parseReceipt(receipt: Base64Receipt): Map<String, String>
}