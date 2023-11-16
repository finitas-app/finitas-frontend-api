package com.finitas.domain.api

import com.finitas.adapters.services.ReceiptService
import com.finitas.domain.model.Receipt
import com.finitas.domain.utils.toByteArray
import com.finitas.exceptions.BadRequestException
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.receiptRouting() {
    val service by inject<ReceiptService>()

    routing {
        route("/receipts") {
            post("/parse") {
                val receipt = call.receiveMultipart().toByteArray()?.let { Receipt(it) }
                    ?: throw BadRequestException("No file provided.")
                call.respond(service.parseReceipt(receipt))
            }
        }
    }
}
