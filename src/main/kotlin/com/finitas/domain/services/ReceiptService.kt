package com.finitas.domain.services

import com.finitas.domain.ports.ReceiptRepository
import io.ktor.http.content.*

class ReceiptService(private val repository: ReceiptRepository) {

    suspend fun parseReceipt(receipt: MultiPartData) = repository.parseReceipt(receipt)
}