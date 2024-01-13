package com.finitas.domain.api

import com.finitas.config.serialization.SerializableUUID
import com.finitas.domain.dto.store.*
import com.finitas.domain.services.UserStoreService
import com.finitas.domain.utils.getPetitioner
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Route.userStoreRouting() {
    val userStoreService by inject<UserStoreService>()

    route("/users") {
        authenticate {
            get("/{idUser}") {
                call.respond(userStoreService.getUser(call.getPetitioner()))
            }
            get {
                call.respond(userStoreService.getUsers(listOf()))
            }
            route("/categories") {
                post("/sync") {
                    val response = userStoreService.syncCategories(
                        call.getPetitioner(),
                        call.receive()
                    )
                    call.respond(HttpStatusCode.OK, response)
                }
                post {
                    userStoreService.addCategories(call.getPetitioner(), call.receive())
                    call.respond(HttpStatusCode.NoContent)
                }
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

@Serializable
data class SyncCategoriesResponse(
    val userCategories: List<UserWithCategoriesDto>,
    val unavailableUsers: List<SerializableUUID>,
)

@Serializable
data class UserWithCategoriesDto(
    val idUser: SerializableUUID,
    val categoryVersion: Int,
    val categories: List<CategoryDto>,
)


@Serializable
data class SyncCategoriesRequest(
    val userVersions: List<CategoryVersionDto>,
)

@Serializable
data class CategoryVersionDto(
    val idUser: SerializableUUID,
    val version: Int,
)

@Serializable
data class ChangeSpendingCategoryDto(
    val spendingCategories: List<SpendingCategoryDto>,
)

@Serializable
data class SpendingCategoryDto(
    val name: String,
    val idParent: SerializableUUID?,
    val idUser: SerializableUUID?,
    val idCategory: SerializableUUID,
    val isDeleted: Boolean,
)
