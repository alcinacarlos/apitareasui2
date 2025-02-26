package com.alcinacarlos.apitareasui.data.model

data class Usuario(
    val _id: String?,
    val username: String,
    val email: String,
    val roles: String? = "USER",
    val direccion: Direccion?
)