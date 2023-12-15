package com.finitas.domain.api

import com.finitas.domain.dto.store.DeleteShoppingListRequest
import com.finitas.domain.dto.store.ShoppingListDto
import com.finitas.domain.dto.store.SynchronizationRequest
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
                userRoleService.authUserByRoleInRoom(call.getPetitioner(), call.getIdRoom(), Permission.DELETE)

                val request = call.receive<SynchronizationRequest<ShoppingListDto>>()
                call.respond(shoppingListStoreService.synchronizeShoppingLists(request))
            }
            get("/{idUser}") {
                userRoleService.authUserByRoleInRoom(call.getPetitioner(), call.getIdRoom(), Permission.READ)

                call.respond(shoppingListStoreService.getAllShoppingLists(call.getIdUser()))
            }
            post {
                userRoleService.authUserByRoleInRoom(call.getPetitioner(), call.getIdRoom(), Permission.WRITE)

                val request = call.receive<ShoppingListDto>()
                call.respond(shoppingListStoreService.createShoppingList(request))
            }
            patch {
                userRoleService.authUserByRoleInRoom(call.getPetitioner(), call.getIdRoom(), Permission.UPDATE)

                val request = call.receive<ShoppingListDto>()
                call.respond(shoppingListStoreService.updateShoppingList(request))
            }
            delete {
                userRoleService.authUserByRoleInRoom(call.getPetitioner(), call.getIdRoom(), Permission.DELETE)

                val request = call.receive<DeleteShoppingListRequest>()
                call.respond(shoppingListStoreService.deleteShoppingList(request))
            }
        }
    }
}