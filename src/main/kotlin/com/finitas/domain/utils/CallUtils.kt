package com.finitas.domain.utils

import com.finitas.config.exceptions.BadRequestException
import com.finitas.config.exceptions.ErrorCode
import io.ktor.server.application.*
import io.ktor.util.*


fun ApplicationCall.getPetitioner() = attributes.get<String>(AttributeKey("userId"))
fun ApplicationCall.getIdRoom() =
    parameters["idRoom"] ?: throw BadRequestException("idRoom not provided", ErrorCode.ID_ROOM_NOT_PROVIDED)

fun ApplicationCall.getIdUser() =
    parameters["idUser"] ?: throw BadRequestException(
        "idUser not provided",
        ErrorCode.ID_USER_NOT_PROVIDED
    )
