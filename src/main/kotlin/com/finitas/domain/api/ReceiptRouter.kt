package com.finitas.domain.api

import com.finitas.domain.services.ReceiptService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.receiptRouting() {
    val service by inject<ReceiptService>()

    route("/receipts") {
        post("/parse") {
            val receipt = call.receiveMultipart()
            call.respond(service.parseReceipt(receipt))
        }
    }
}
