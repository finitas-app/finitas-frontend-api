package com.finitas.domain.ports

import com.finitas.config.serialization.SerializableUUID
import com.finitas.domain.api.AddRoleRequest
import com.finitas.domain.api.DeleteRoleRequest
import com.finitas.domain.api.UpdateRoleRequest
import com.finitas.domain.dto.store.RoomRoleDto
import com.finitas.domain.dto.store.UsersToNotifyResponse
import kotlinx.serialization.Serializable
import java.util.*

interface RoleRepository {
    suspend fun addRole(requester: UUID, addRoleRequest: AddRoleRequest): AddRoleResponse
    suspend fun updateRole(requester: UUID, updateRoleRequest: UpdateRoleRequest): UpdateRoleResponse
    suspend fun deleteRole(requester: UUID, deleteRoleRequest: DeleteRoleRequest): UsersToNotifyResponse
}

@Serializable
data class AddRoleResponse(
    val roomRole: RoomRoleDto,
    val usersToNotify: List<SerializableUUID>,
)

@Serializable
data class UpdateRoleResponse(
    val roomRole: RoomRoleDto,
    val usersToNotify: List<SerializableUUID>,
)