package com.finitas.domain.utils

import com.finitas.config.exceptions.InternalServerException
import io.ktor.http.content.*

suspend fun MultiPartData.toByteArray(): ByteArray? {
    try {
        var result: ByteArray? = null
        this.forEachPart { part ->
            if (part is PartData.FileItem) {
                result = part.streamProvider().readBytes()
            }
            part.dispose()
        }

        return result
    } catch (exception: Exception) {
        throw InternalServerException("Failed to convert receipt to file", exception)
    }
}