package com.finitas.domain.api

import com.finitas.domain.dto.store.DeleteFinishedSpendingRequest
import com.finitas.domain.dto.store.FinishedSpendingDto
import com.finitas.domain.dto.store.SynchronizationRequest
import com.finitas.domain.model.Permission
import com.finitas.domain.services.FinishedSpendingStoreService
import com.finitas.domain.services.UserRoleService
import com.finitas.domain.utils.getIdRoom
import com.finitas.domain.utils.getIdUser
import com.finitas.domain.utils.getPetitioner
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
            put("/synchronize") {
                userRoleService.authUserByRoleInRoom(call.getPetitioner(), call.getIdRoom(), Permission.DELETE)

                val request = call.receive<SynchronizationRequest<FinishedSpendingDto>>()
                call.respond(finishedSpendingsService.synchronizeFinishedSpendings(request))
            }
            get("/{idUser}") {
                userRoleService.authUserByRoleInRoom(call.getPetitioner(), call.getIdRoom(), Permission.READ)

                call.respond(finishedSpendingsService.getAllFinishedSpendings(call.getIdUser()))
            }
            post {
                userRoleService.authUserByRoleInRoom(call.getPetitioner(), call.getIdRoom(), Permission.WRITE)

                val request = call.receive<FinishedSpendingDto>()
                call.respond(finishedSpendingsService.createFinishedSpending(request))
            }
            patch {
                userRoleService.authUserByRoleInRoom(call.getPetitioner(), call.getIdRoom(), Permission.UPDATE)

                val request = call.receive<FinishedSpendingDto>()
                call.respond(finishedSpendingsService.updateFinishedSpending(request))
            }
            delete {
                userRoleService.authUserByRoleInRoom(call.getPetitioner(), call.getIdRoom(), Permission.DELETE)

                val request = call.receive<DeleteFinishedSpendingRequest>()
                call.respond(finishedSpendingsService.deleteFinishedSpending(request))
            }
        }
    }
}