package com.jazart.smarthome.models

data class User(val name: String,
                val id: String,
                val family: List<User>,
                val role: Role.Parent)

sealed class Role {
    object Parent : Role()
    object Child : Role()
}

