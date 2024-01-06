package com.finitas.domain.model

import com.finitas.config.serialization.SerializableBigDecimal
import kotlinx.serialization.Serializable

@Serializable
data class Base64Receipt(
    val value: String
)

@Serializable
data class ParsedEntry(val title: String, val number: SerializableBigDecimal)

@Serializable
data class ReceiptParseResult(val entries: List<ParsedEntry>)
