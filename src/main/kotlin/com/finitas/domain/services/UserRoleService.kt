package com.finitas.domain.services

import com.finitas.config.exceptions.ErrorCode
import com.finitas.config.exceptions.ForbiddenException
import com.finitas.domain.model.Permission
import com.finitas.domain.ports.UserRoleRepository

class UserRoleService(private val repository: UserRoleRepository) {
    fun authUserByRoleInRoom(idUser: String, idRoom: String, requiredPermission: Permission) {
        val userRole = repository.getUserRoleInRoom(idUser, idRoom)
        if (userRole.permission.ordinal < requiredPermission.ordinal) throw ForbiddenException(
            "User not allowed to perform this action",
            ErrorCode.ACTION_FORBIDDEN_ERROR
        )
    }
}