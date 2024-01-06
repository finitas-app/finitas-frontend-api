package com.finitas.domain.services

import com.finitas.domain.model.Permission
import com.finitas.domain.ports.UserRoleRepository
import java.util.*

class UserRoleService(private val repository: UserRoleRepository) {
    fun authUserByRoleInRoom(idUser: UUID, requiredPermission: Permission) {
        // todo: dania - implement proper auth
        //val userRole = repository.getUserRoleInRoom(idUser, idRoom)
//        if (userRole.permission.ordinal < requiredPermission.ordinal) throw ForbiddenException(
//            "User not allowed to perform this action",
//            ErrorCode.ACTION_FORBIDDEN_ERROR
//        )
    }
}