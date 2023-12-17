package com.finitas.domain.ports

import com.finitas.domain.model.Role
import java.util.*

interface UserRoleRepository {
    fun getUserRoleInRoom(idUser: UUID, idRoom: UUID): Role
}