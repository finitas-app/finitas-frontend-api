package com.finitas.domain.services

import com.finitas.domain.api.AddRoleRequest
import com.finitas.domain.api.DeleteRoleRequest
import com.finitas.domain.api.UpdateRoleRequest
import com.finitas.domain.ports.*
import java.util.*

class RoomRolesService(
    private val roleRepository: RoleRepository,
    private val notifierPort: UserNotifierPort,
) {
    suspend fun addRole(requester: UUID, addRoleRequest: AddRoleRequest) {
        val response = roleRepository.addRole(requester, addRoleRequest)
        //todo: notify partly but not whole room
        notifierPort.notifyUser(
            UserNotificationDto(
                UserNotificationEvent.SYNC_ROOM,
                listOf(TargetUsersNotificationDto(response.usersToNotify))
            )
        )
    }

    suspend fun updateRole(requester: UUID, updateRoleRequest: UpdateRoleRequest) {
        val response = roleRepository.updateRole(requester, updateRoleRequest)
        //todo: notify partly but not whole room
        notifierPort.notifyUser(
            UserNotificationDto(
                UserNotificationEvent.SYNC_ROOM,
                listOf(TargetUsersNotificationDto(response.usersToNotify))
            )
        )

    }

    suspend fun deleteRole(requester: UUID, deleteRoleRequest: DeleteRoleRequest) {
        val response = roleRepository.deleteRole(requester, deleteRoleRequest)
        //todo: notify partly but not whole room
        notifierPort.notifyUser(
            UserNotificationDto(
                UserNotificationEvent.SYNC_ROOM,
                listOf(TargetUsersNotificationDto(response.usersToNotify))
            )
        )
    }
}