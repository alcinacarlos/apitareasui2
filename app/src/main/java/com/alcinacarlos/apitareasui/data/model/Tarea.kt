package com.alcinacarlos.apitareasui.data.model

import java.time.LocalDate

data class Tarea(
    val _id: String?,
    val titulo: String,
    val descripcion: String,
    val usuarioId: String,
    val completada: Boolean,
    val fechaCreaccion: String
)