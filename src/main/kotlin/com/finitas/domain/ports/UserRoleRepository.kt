package com.finitas.domain.ports

import com.finitas.domain.model.Role

interface UserRoleRepository {
    fun getUserRoleInRoom(idUser: String, idRoom: String): Role
}