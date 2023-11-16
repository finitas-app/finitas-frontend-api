package com.finitas.domain.services

import com.finitas.domain.model.Receipt
import com.finitas.domain.ports.ReceiptRepository

class ReceiptService(private val repository: ReceiptRepository) {

    suspend fun parseReceipt(receipt: Receipt) = repository.parseReceipt(receipt)
}