package com.finitas.domain.api

import io.ktor.server.routing.*

fun Route.storeRouting() {
    route("/api/store") {
        finishedSpendingStoreRouting()
        shoppingListStoreRouting()
        userStoreRouting()
    }
}