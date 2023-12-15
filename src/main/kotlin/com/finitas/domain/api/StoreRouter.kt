package com.finitas.domain.api

import io.ktor.server.routing.*

fun Route.storeRouting() {
    route("/store") {
        finishedSpendingStoreRouting()
        shoppingListStoreRouting()
        userStoreRouting()
    }
}