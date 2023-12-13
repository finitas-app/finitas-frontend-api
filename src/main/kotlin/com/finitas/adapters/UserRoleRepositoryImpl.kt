package com.finitas.adapters

import com.finitas.domain.model.Permission
import com.finitas.domain.model.Role
import com.finitas.domain.ports.UserRoleRepository

class UserRoleRepositoryImpl : UserRoleRepository {
    override fun getUserRoleInRoom(idUser: String, idRoom: String): Role {
        return Role(Permission.DELETE)
    }
}