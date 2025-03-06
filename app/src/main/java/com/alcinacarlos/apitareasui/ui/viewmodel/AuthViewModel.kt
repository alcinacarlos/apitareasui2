package com.alcinacarlos.apitareasui.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alcinacarlos.apitareasui.data.remote.RetrofitInstance
import com.alcinacarlos.apitareasui.dto.LoginUsuarioDTO
import com.alcinacarlos.apitareasui.dto.UsuarioRegisterDTO
import com.alcinacarlos.apitareasui.utils.Utils.parseApiError
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

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> get() = _registrationSuccess

    private val api = RetrofitInstance.getInstance()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = api.login(LoginUsuarioDTO(username, password))
                if (!response.isSuccessful) {
                    _error.value = "Contraseña y/o usuario incorrecto"
                } else {
                    val newToken = response.body()?.token ?: ""
                    _token.value = newToken
                    RetrofitInstance.changeToken(newToken)
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
                if (response.isSuccessful) {
                    _registrationSuccess.value = true
                    _loading.value = false

                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = parseApiError(errorBody)
                    _error.value = errorMessage
                    _loading.value = false
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
                _loading.value = false
            }
        }
    }


}