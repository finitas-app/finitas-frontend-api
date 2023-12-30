package com.finitas.domain.ports

import java.util.*

interface ReachableUsersRepository {

    suspend fun getReachableUsersForUser(idUser: UUID, idRoom: UUID?): ReachableUsersDto
}