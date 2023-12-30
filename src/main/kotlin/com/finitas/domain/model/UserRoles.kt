package com.finitas.domain.model

enum class Permission {
    READ_USERS_DATA,
    MODIFY_USERS_DATA,
}

data class Role(
    val permission: Permission
)