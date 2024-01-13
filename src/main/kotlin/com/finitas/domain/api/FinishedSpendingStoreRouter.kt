package com.finitas.domain.api

import com.finitas.domain.dto.store.DeleteFinishedSpendingRequest
import com.finitas.domain.dto.store.FinishedSpendingDto
import com.finitas.domain.dto.store.IdUserWithVersion
import com.finitas.domain.services.FinishedSpendingStoreService
import com.finitas.domain.utils.getIdUser
import com.finitas.domain.utils.getPetitioner
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.finishedSpendingStoreRouting() {
    val finishedSpendingsService by inject<FinishedSpendingStoreService>()

    route("/finished-spendings") {
        authenticate {
            route("/sync") {
                put {
                    val request = call.receive<List<FinishedSpendingDto>>()
                    finishedSpendingsService.updateWithChangedItems(
                        request,
                        call.getPetitioner()
                    )
                    call.respond(HttpStatusCode.NoContent)
                }
                post {
                    val request = call.receive<List<IdUserWithVersion>>()
                    call.respond(
                        finishedSpendingsService.fetchUsersUpdates(call.getPetitioner(), request)
                    )
                }
            }
            get("/{idUser}") {

                call.respond(finishedSpendingsService.getAllFinishedSpendings(call.getPetitioner(), call.getIdUser()))
            }
            post {

                val request = call.receive<FinishedSpendingDto>()
                call.respond(finishedSpendingsService.createFinishedSpending(call.getPetitioner(), request))
            }
            patch {

                val request = call.receive<FinishedSpendingDto>()
                call.respond(finishedSpendingsService.updateFinishedSpending(call.getPetitioner(), request))
            }
            delete {

                val request = call.receive<DeleteFinishedSpendingRequest>()
                call.respond(finishedSpendingsService.deleteFinishedSpending(call.getPetitioner(), request))
            }
        }
    }
}