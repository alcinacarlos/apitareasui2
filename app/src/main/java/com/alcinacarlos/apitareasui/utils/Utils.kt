package com.alcinacarlos.apitareasui.utils

import com.alcinacarlos.apitareasui.data.model.ApiError
import com.google.gson.Gson

object Utils {
    fun parseApiError(errorBody: String?): String {
        return try {
            val apiError = Gson().fromJson(errorBody, ApiError::class.java)
            apiError?.message ?: "Error desconocido"
        } catch (e: Exception) {
            "Error al procesar la respuesta del servidor"
        }
    }
}