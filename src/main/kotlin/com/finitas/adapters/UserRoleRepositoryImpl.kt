package com.finitas.adapters

import com.finitas.domain.model.Permission
import com.finitas.domain.model.Role
import com.finitas.domain.ports.UserRoleRepository
import java.util.*

class UserRoleRepositoryImpl : UserRoleRepository {
    override fun getUserRoleInRoom(idUser: UUID, idRoom: UUID): Role {
        // todo: dania - create proper role fetch
        return Role(Permission.READ_USERS_DATA)
    }
}