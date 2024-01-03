package com.finitas.domain.api

import com.finitas.domain.dto.store.*
import com.finitas.domain.model.Permission
import com.finitas.domain.services.FinishedSpendingStoreService
import com.finitas.domain.services.UserRoleService
import com.finitas.domain.utils.getIdRoom
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
    val userRoleService by inject<UserRoleService>()

    route("/finished-spendings") {
        authenticate {
            put {
                val request = call.receive<List<FinishedSpendingDto>>()
                finishedSpendingsService.updateWithChangedItems(
                    request,
                    call.getPetitioner()
                )
                call.respond(HttpStatusCode.NoContent)
            }
            get {
                // todo: verify allowance to fetch users
                val request = call.receive<List<IdUserWithVersion>>()
                call.respond(
                    finishedSpendingsService.fetchUsersUpdates(request)
                )
            }
            get("/{idUser}") {
                userRoleService.authUserByRoleInRoom(call.getPetitioner(), call.getIdRoom(), Permission.READ_USERS_DATA)

                call.respond(finishedSpendingsService.getAllFinishedSpendings(call.getIdUser()))
            }
            post {
                userRoleService.authUserByRoleInRoom(call.getPetitioner(), call.getIdRoom(), Permission.MODIFY_USERS_DATA)

                val request = call.receive<FinishedSpendingDto>()
                call.respond(finishedSpendingsService.createFinishedSpending(request))
            }
            patch {
                userRoleService.authUserByRoleInRoom(call.getPetitioner(), call.getIdRoom(), Permission.MODIFY_USERS_DATA)

                val request = call.receive<FinishedSpendingDto>()
                call.respond(finishedSpendingsService.updateFinishedSpending(request))
            }
            delete {
                userRoleService.authUserByRoleInRoom(call.getPetitioner(), call.getIdRoom(), Permission.MODIFY_USERS_DATA)

                val request = call.receive<DeleteFinishedSpendingRequest>()
                call.respond(finishedSpendingsService.deleteFinishedSpending(request))
            }
        }
    }
}