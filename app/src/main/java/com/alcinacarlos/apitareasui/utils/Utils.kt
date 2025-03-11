package com.alcinacarlos.apitareasui.utils

import com.alcinacarlos.apitareasui.data.model.ApiError
import com.google.gson.Gson

/**
 * Objeto de utilidad que proporciona funciones auxiliares para el manejo de errores y respuestas de la API.
 */
object Utils {

    /**
     * Parsea el cuerpo de error de la respuesta de la API y extrae el mensaje de error.
     *
     * @param errorBody Cuerpo de la respuesta de error en formato JSON.
     * @return El mensaje de error extraído o un mensaje genérico si el formato es inválido.
     */
    fun parseApiError(errorBody: String?): String {
        return try {
            val apiError = Gson().fromJson(errorBody, ApiError::class.java)
            apiError?.message ?: "Error desconocido"
        } catch (e: Exception) {
            "Error al procesar la respuesta del servidor"
        }
    }
}
