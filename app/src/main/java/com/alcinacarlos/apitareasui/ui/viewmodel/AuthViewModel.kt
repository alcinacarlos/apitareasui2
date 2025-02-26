package com.alcinacarlos.apitareasui.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alcinacarlos.apitareasui.data.model.ApiError
import com.alcinacarlos.apitareasui.dto.LoginUsuarioDTO
import com.alcinacarlos.apitareasui.dto.UsuarioRegisterDTO
import com.alcinacarlos.apitareasui.data.remote.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val api: ApiService) : ViewModel() {

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> get() = _token

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = api.login(LoginUsuarioDTO(username, password))
                _token.value = response.token
            } catch (e: Exception) {
                _error.value = "Error de autenticación"
            }
            _loading.value = false
        }
    }

    fun test(user: UsuarioRegisterDTO) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                api.register(user)
            } catch (e: Exception) {
                println(e)
                _error.value = "${e.message}"
            }
            _loading.value = false
        }
    }
    fun register(user: UsuarioRegisterDTO) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = api.register(user)
                if (!response.isSuccessful) {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = parseApiError(errorBody)
                    _error.value = errorMessage
                }
            } catch (e: Exception) {
                println(e)
                _error.value = "Error de conexión"
            }
            _loading.value = false
        }

    }

    // Función para convertir el JSON de error en un mensaje de usuario usando Gson
    fun parseApiError(errorBody: String?): String {
        return try {
            val apiError = Gson().fromJson(errorBody, ApiError::class.java)
            val fullMessage = apiError?.message ?: "Error desconocido"

            // Dividir el mensaje después del primer punto y tomar solo la segunda parte
            val splitMessage = fullMessage.split(".", limit = 2)
            if (splitMessage.size > 1) splitMessage[1].trim() else fullMessage
        } catch (e: Exception) {
            "Error al procesar la respuesta del servidor"
        }
    }
}
