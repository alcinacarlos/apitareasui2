package com.alcinacarlos.apitareasui.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alcinacarlos.apitareasui.data.model.ApiError
import com.alcinacarlos.apitareasui.data.remote.ApiService
import com.alcinacarlos.apitareasui.dto.LoginUsuarioDTO
import com.alcinacarlos.apitareasui.dto.UsuarioRegisterDTO
import com.alcinacarlos.apitareasui.data.remote.RetrofitInstance
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {
    private val _token = MutableStateFlow("")
    val token: StateFlow<String> get() = _token

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    private val api = RetrofitInstance.getInstance()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = api.login(LoginUsuarioDTO(username, password))
                if (!response.isSuccessful) {
                    _error.value = "Contraseña y/o usuario incorrecto"
                }else{
                    RetrofitInstance.changeToken(response.body()?.token)
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
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
            apiError?.message ?: "Error desconocido"
        } catch (e: Exception) {
            "Error al procesar la respuesta del servidor"
        }
    }
}
