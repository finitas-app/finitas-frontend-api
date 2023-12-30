package com.finitas.domain.api

import com.finitas.domain.dto.store.*
import com.finitas.domain.services.UserStoreService
import com.finitas.domain.utils.getPetitioner
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userStoreRouting() {
    val userStoreService by inject<UserStoreService>()

    route("/users") {
        authenticate {
            get("/{idUser}") {
                call.respond(userStoreService.getUser(call.getPetitioner()))
            }
            get {
                // todo: dania - fetch only allowed users
                call.respond(userStoreService.getUsers(listOf()))
            }
            route("/nicknames") {
                post("/sync") {
                    val request = call.receive<GetVisibleNamesRequest>()
                    val currentUserId = call.getPetitioner()
                    call.respond(userStoreService.getNicknames(currentUserId, request))
                }
                patch {
                    userStoreService.updateNickname(
                        IdUserWithVisibleName(
                            idUser = call.getPetitioner(),
                            visibleName = call.receive<VisibleName>().value
                        )
                    )
                    call.respond(HttpStatusCode.NoContent)
                }
            }
            route("/regular-spendings") {
                post {
                    val insertResult = userStoreService.addRegularSpending(
                        call.getPetitioner(),
                        call.receive<List<RegularSpendingDto>>()
                    )
                    call.respond(insertResult)
                }
                get {
                    val insertResult = userStoreService.getRegularSpendings(
                        call.getPetitioner(),
                    )
                    call.respond(insertResult)
                }
                delete {
                    userStoreService.deleteRegularSpending(
                        idUser = call.getPetitioner(),
                        idSpendingSummary = call.receive<IdSpendingSummary>().idSpendingSummary
                    )
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}