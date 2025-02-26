package com.alcinacarlos.apitareasui.dto

import com.alcinacarlos.apitareasui.data.model.Direccion

data class UsuarioRegisterDTO(
    val username: String,
    val email: String,
    val password: String,
    val passwordRepeat: String,
    val rol: String?,
    val direccion: Direccion
)
