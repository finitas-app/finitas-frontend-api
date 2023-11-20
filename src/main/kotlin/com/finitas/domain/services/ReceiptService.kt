package com.finitas.domain.services

import com.finitas.domain.model.ReceiptBinaryData
import com.finitas.domain.ports.ReceiptRepository
import io.ktor.http.content.*

class ReceiptService(private val repository: ReceiptRepository) {

    suspend fun parseReceipt(receipt: ReceiptBinaryData) = repository.parseReceipt(receipt)
}