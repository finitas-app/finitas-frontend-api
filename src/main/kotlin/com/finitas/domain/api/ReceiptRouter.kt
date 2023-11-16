package com.finitas.domain.api

import com.finitas.config.exceptions.BadRequestException
import com.finitas.config.exceptions.ErrorCode
import com.finitas.domain.model.Receipt
import com.finitas.domain.services.ReceiptService
import com.finitas.domain.utils.toByteArray
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import kotlin.math.log

fun Route.receiptRouting() {
    val service by inject<ReceiptService>()

    route("/receipts") {
        post("/parse") {
            val receipt = call.receiveMultipart().toByteArray()?.let { Receipt(it) }
                ?: throw BadRequestException("No file provided.", ErrorCode.NOT_FILE_PROVIDED)
            call.respond(service.parseReceipt(receipt))
        }
    }
}
