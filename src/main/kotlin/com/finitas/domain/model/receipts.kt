package com.finitas.domain.model

import io.ktor.client.request.forms.*
import io.ktor.http.content.*
import kotlinx.serialization.Serializable

@JvmInline
value class ReceiptBinaryData(val raw: MultiPartData) {
    suspend fun toMultiPartFormDataContent() = MultiPartFormDataContent(raw.readAllParts())
}

@Serializable
data class ReceiptParseResult(val result: Map<String, String>)
