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

/**
 * ViewModel encargado de manejar la autenticación de usuarios.
 * Proporciona funcionalidades de inicio de sesión y registro de usuarios.
 */
class AuthViewModel : ViewModel() {

    /** Estado del token de autenticación. */
    private val _token = MutableStateFlow("")
    val token: StateFlow<String> get() = _token

    /** Estado de carga para indicar si una operación está en progreso. */
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    /** Estado para almacenar mensajes de error en la autenticación. */
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    /** Estado que indica si el registro fue exitoso. */
    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> get() = _registrationSuccess

    /** Instancia de la API para realizar las solicitudes de autenticación. */
    private val api = RetrofitInstance.getInstance()

    /**
     * Realiza el inicio de sesión de un usuario.
     * @param username Nombre de usuario.
     * @param password Contraseña del usuario.
     */
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

    /**
     * Registra un nuevo usuario.
     * @param user Datos del usuario a registrar.
     */
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
