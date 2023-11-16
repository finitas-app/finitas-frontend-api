package com.finitas.adapters.services

import com.finitas.adapters.repositories.ReceiptRepositoryImpl
import com.finitas.domain.model.Receipt
import com.finitas.domain.ports.ReceiptRepository

class ReceiptService(private val repository: ReceiptRepository = ReceiptRepositoryImpl()) {

    suspend fun parseReceipt(receipt: Receipt) = repository.parseReceipt(receipt)
}