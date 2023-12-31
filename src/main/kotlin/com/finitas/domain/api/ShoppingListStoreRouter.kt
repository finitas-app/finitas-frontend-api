package com.finitas.domain.api

import com.finitas.domain.dto.store.DeleteShoppingListRequest
import com.finitas.domain.dto.store.ShoppingListDto
import com.finitas.domain.dto.store.SynchronizationRequestFromClient
import com.finitas.domain.model.Permission
import com.finitas.domain.services.ShoppingListStoreService
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

fun Route.shoppingListStoreRouting() {
    val shoppingListStoreService by inject<ShoppingListStoreService>()
    val userRoleService by inject<UserRoleService>()

    route("/shopping-lists") {
        authenticate {
            put("/synchronize") {
                val request = call.receive<SynchronizationRequestFromClient<ShoppingListDto>>()

                val requiredPermission =
                    if (request.objects.isEmpty()) Permission.READ_USERS_DATA else Permission.MODIFY_USERS_DATA

                // TODO: clear or reformat
                /*userRoleService.authUserByRoleInRoom(
                    call.getPetitioner(),
                    call.getIdRoom(),
                    requiredPermission
                )*/

                call.respond(shoppingListStoreService.synchronizeShoppingLists(
                    request.mapToStoreRequest(call.getPetitioner())
                ))
            }
            get("/{idUser}") {
                userRoleService.authUserByRoleInRoom(call.getPetitioner(), call.getIdRoom(), Permission.READ_USERS_DATA)

                call.respond(shoppingListStoreService.getAllShoppingLists(call.getIdUser()))
            }
            post {
                //TODO: add verify
                /*userRoleService.authUserByRoleInRoom(
                    call.getPetitioner(),
                    call.getIdRoom(),
                    Permission.MODIFY_USERS_DATA
                )*/

                val request = call.receive<ShoppingListDto>()
                call.respond(shoppingListStoreService.createShoppingList(request))
            }
            patch {
                //TODO: add verify
               /* userRoleService.authUserByRoleInRoom(
                    call.getPetitioner(),
                    call.getIdRoom(),
                    Permission.MODIFY_USERS_DATA
                )*/

                val request = call.receive<ShoppingListDto>()
                call.respond(shoppingListStoreService.updateShoppingList(request))
            }
            delete {
                userRoleService.authUserByRoleInRoom(
                    call.getPetitioner(),
                    call.getIdRoom(),
                    Permission.MODIFY_USERS_DATA
                )

                val request = call.receive<DeleteShoppingListRequest>()
                call.respond(shoppingListStoreService.deleteShoppingList(request))
            }
        }
    }
}