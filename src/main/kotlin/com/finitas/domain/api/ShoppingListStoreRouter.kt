package com.finitas.domain.api

import com.finitas.domain.dto.store.DeleteShoppingListRequest
import com.finitas.domain.dto.store.IdUserWithVersion
import com.finitas.domain.dto.store.ShoppingListDto
import com.finitas.domain.services.ShoppingListStoreService
import com.finitas.domain.utils.getIdUser
import com.finitas.domain.utils.getPetitioner
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.shoppingListStoreRouting() {
    val shoppingListStoreService by inject<ShoppingListStoreService>()

    route("/shopping-lists") {
        authenticate {
            route("/sync") {
                put {
                    val request = call.receive<List<ShoppingListDto>>()
                    shoppingListStoreService.updateWithChangedItems(
                        request,
                        call.getPetitioner()
                    )
                    call.respond(HttpStatusCode.NoContent)
                }
                post {
                    val request = call.receive<List<IdUserWithVersion>>()
                    call.respond(
                        HttpStatusCode.OK,
                        shoppingListStoreService.fetchUsersUpdates(request)
                    )
                }
            }
            get("/{idUser}") {

                call.respond(shoppingListStoreService.getAllShoppingLists(call.getIdUser()))
            }
            post {
                val request = call.receive<ShoppingListDto>()
                call.respond(shoppingListStoreService.createShoppingList(call.getPetitioner(), request))
            }
            patch {

                val request = call.receive<ShoppingListDto>()
                call.respond(shoppingListStoreService.updateShoppingList(call.getPetitioner(), request))
            }
            delete {

                val request = call.receive<DeleteShoppingListRequest>()
                call.respond(shoppingListStoreService.deleteShoppingList(call.getPetitioner(), request))
            }
        }
    }
}