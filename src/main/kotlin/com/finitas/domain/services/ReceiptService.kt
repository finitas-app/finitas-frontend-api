package com.finitas.domain.services

import com.finitas.domain.model.Base64Receipt
import com.finitas.domain.model.ParsedEntry
import com.finitas.domain.model.ReceiptParseResult
import com.finitas.domain.ports.ReceiptRepository
import java.math.BigDecimal

private val productNamePredicates = listOf<(String) -> Boolean>(
    { str -> !str.contains("suma", true) },
    { str -> !str.contains("sprzeda", true) },
    { str -> !str.contains("PTU") },
)

class ReceiptService(private val repository: ReceiptRepository) {
    suspend fun parseReceipt(receipt: Base64Receipt) =
        repository.parseReceipt(receipt)
            .filter { (key, _) -> productNamePredicates.all { it(key) } }
            .map { (key, value) ->
                ParsedEntry(
                    title = key,
                    number = value
                        .replace(" ", "")
                        .replace(",", ".")
                        .toBigDecimalOrNull()
                        ?: BigDecimal.ZERO,
                )
            }
            .let { ReceiptParseResult(it) }
}