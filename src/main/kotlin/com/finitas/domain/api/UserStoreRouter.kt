package com.finitas.domain.api

import com.finitas.domain.dto.store.GetVisibleNamesRequest
import com.finitas.domain.dto.store.RegularSpendingDto
import com.finitas.domain.dto.store.UserDataDto
import com.finitas.domain.dto.store.UserDto
import com.finitas.domain.model.Permission
import com.finitas.domain.services.UserRoleService
import com.finitas.domain.services.UserStoreService
import com.finitas.domain.utils.getIdRoom
import com.finitas.domain.utils.getPetitioner
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userStoreRouting() {
    val userStoreService by inject<UserStoreService>()
    val userRoleService by inject<UserRoleService>()

    route("/users") {
        authenticate {
            put {
                val request = call.receive<UserDataDto>()
                call.respond(userStoreService.upsertUser(UserDto(
                    idUser = call.getPetitioner(),
                    visibleName = request.visibleName,
                    regularSpendings = request.regularSpendings
                )))
            }
            post("/regular-spendings") {
                val insertResult = userStoreService.addRegularSpending(
                    call.getPetitioner(),
                    call.receive<List<RegularSpendingDto>>()
                )
                call.respond(insertResult)
            }
            get("/regular-spendings") {
                val insertResult = userStoreService.getRegularSpendings(
                    call.getPetitioner(),
                )
                call.respond(insertResult)
            }
            get("/nicknames") {
                userRoleService.authUserByRoleInRoom(call.getPetitioner(), call.getIdRoom(), Permission.READ)

                val request = call.receive<GetVisibleNamesRequest>()
                call.respond(userStoreService.getNicknames(request))
            }
        }
    }
}