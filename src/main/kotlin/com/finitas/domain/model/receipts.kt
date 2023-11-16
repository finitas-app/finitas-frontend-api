package com.finitas.domain.model

import kotlinx.serialization.Serializable
import java.io.File

data class Receipt(val file: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Receipt

        return file.contentEquals(other.file)
    }

    override fun hashCode(): Int {
        return file.contentHashCode()
    }
}

@Serializable
data class ReceiptParseResult(val result: Map<String, String>)
