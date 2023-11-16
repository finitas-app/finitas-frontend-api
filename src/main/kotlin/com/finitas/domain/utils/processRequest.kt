package com.finitas.domain.utils

import com.finitas.config.Logger
import com.finitas.exceptions.InternalServerException
import io.ktor.http.content.*
import java.io.File

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
    }
    catch (exception: Exception) {
        Logger.error("Failed to convert receipt to file")
        exception.stackTrace.forEach { traceLine -> Logger.error(traceLine.toString())}
        throw InternalServerException("Failed to convert receipt to file")
    }
}