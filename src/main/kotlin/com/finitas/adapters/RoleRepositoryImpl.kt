package com.finitas.adapters

import com.finitas.config.httpClient
import com.finitas.config.serialization.SerializableUUID
import com.finitas.config.setRoomAuthorization
import com.finitas.config.urls.UrlProvider
import com.finitas.domain.api.AddRoleRequest
import com.finitas.domain.api.DeleteRoleRequest
import com.finitas.domain.api.UpdateRoleRequest
import com.finitas.domain.dto.store.Authority
import com.finitas.domain.dto.store.UsersToNotifyResponse
import com.finitas.domain.ports.AddRoleResponse
import com.finitas.domain.ports.RoleRepository
import com.finitas.domain.ports.UpdateRoleResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import java.util.*

class RoleRepositoryImpl(
    private val urlProvider: UrlProvider,
) : RoleRepository {
    override suspend fun addRole(requester: UUID, addRoleRequest: AddRoleRequest): AddRoleResponse {
        return httpClient.post("${urlProvider.ROOM_MANAGER_HOST_URL}/rooms/roles") {
            setRoomAuthorization(requester, addRoleRequest.idRoom)
            setBody(addRoleRequest.toDto())
        }.body()
    }

    override suspend fun updateRole(requester: UUID, updateRoleRequest: UpdateRoleRequest): UpdateRoleResponse {
        return httpClient.put("${urlProvider.ROOM_MANAGER_HOST_URL}/rooms/roles") {
            setRoomAuthorization(requester, updateRoleRequest.idRoom)
            setBody(updateRoleRequest.toDto())
        }.body()
    }

    override suspend fun deleteRole(requester: UUID, deleteRoleRequest: DeleteRoleRequest): UsersToNotifyResponse {
        return httpClient.delete("${urlProvider.ROOM_MANAGER_HOST_URL}/rooms/roles") {
            setRoomAuthorization(requester, deleteRoleRequest.idRoom)
            setBody(deleteRoleRequest.toDto())
        }.body()
    }
}

@Serializable
data class AddRoleDto(
    val name: String,
    val authorities: Set<Authority>,
)

@Serializable
data class UpdateRoleDto(
    val idRole: SerializableUUID,
    val name: String,
    val authorities: Set<Authority>,
)

@Serializable
data class DeleteRoleDto(
    val idRole: SerializableUUID,
)
