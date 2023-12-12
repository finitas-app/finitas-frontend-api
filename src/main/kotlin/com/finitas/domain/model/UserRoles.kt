package com.finitas.domain.model

enum class Permission {
    READ,
    WRITE,
    UPDATE,
    DELETE,
}

data class Role(
    val permissions: List<Permission>
)