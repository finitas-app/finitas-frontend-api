package com.finitas.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ReceiptParseResult(val result: Map<String, String>)
