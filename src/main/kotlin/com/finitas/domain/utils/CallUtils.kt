package com.finitas.domain.utils

import com.finitas.config.exceptions.BadRequestException
import com.finitas.config.exceptions.ErrorCode
import io.ktor.server.application.*
import io.ktor.util.*
import java.util.*

fun ApplicationCall.getPetitioner() = attributes.get<UUID>(AttributeKey("userId"))

fun ApplicationCall.getIdRoom() =
    try {
        parameters["idRoom"]?.let { UUID.fromString(it) }
    } catch (_: Exception) {
        throw BadRequestException("idRoom is not valid", ErrorCode.ID_ROOM_NOT_PROVIDED)
    }
        ?: throw BadRequestException("idRoom not provided", ErrorCode.ID_ROOM_NOT_PROVIDED)

fun ApplicationCall.getIdUser() =
    try {
        parameters["idUser"]?.let { UUID.fromString(it) }
    } catch (_: Exception) {
        throw BadRequestException(
            "idUser not provided",
            ErrorCode.ID_USER_NOT_PROVIDED
        )
    }
        ?: throw BadRequestException(
            "idUser not provided",
            ErrorCode.ID_USER_NOT_PROVIDED
        )
