package com.alcinacarlos.apitareasui.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alcinacarlos.apitareasui.data.model.Tarea
import com.alcinacarlos.apitareasui.data.remote.RetrofitInstance
import com.alcinacarlos.apitareasui.dto.TareaInsertDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona la lógica de la pantalla de tareas.
 * Se encarga de obtener, agregar, actualizar y eliminar tareas.
 */
class TaskViewModel(authViewModel: AuthViewModel) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    /** Estado que indica si la aplicación está cargando datos. */
    val loading: StateFlow<Boolean> get() = _loading

    private val _error = MutableStateFlow<String?>(null)
    /** Estado para manejar errores generales. */
    val error: StateFlow<String?> get() = _error

    private val _errorDialog = MutableStateFlow<String?>(null)
    /** Estado para manejar errores específicos de diálogos. */
    val errorDialog: StateFlow<String?> get() = _errorDialog

    private val _registersucessful = MutableStateFlow(false)
    /** Estado que indica si el registro de una nueva tarea fue exitoso. */
    val registersucessful: StateFlow<Boolean> get() = _registersucessful

    private var api = RetrofitInstance.getInstance()

    private val _tareas = MutableStateFlow<List<Tarea>>(emptyList())
    /** Lista de tareas obtenidas del servidor. */
    val tareas: StateFlow<List<Tarea>> = _tareas

    init {
        viewModelScope.launch {
            authViewModel.token.collect { token ->
                RetrofitInstance.changeToken(token)
                api = RetrofitInstance.getInstance()
                obtenerTareas()
            }
        }
    }

    /**
     * Obtiene la lista de tareas desde la API y actualiza el estado.
     */
    fun obtenerTareas() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val response = api.obtenerTareas()
                if (response.isSuccessful) {
                    _tareas.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error al obtener las tareas"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
            }
            _loading.value = false
        }
    }

    /**
     * Marca una tarea como completada o no completada.
     * @param tarea Tarea que se actualizará.
     */
    fun marcarComoHecha(tarea: Tarea) {
        viewModelScope.launch {
            try {
                api.marcarComoHecha(tarea._id!!)
                _tareas.value = _tareas.value.map {
                    if (it._id == tarea._id) it.copy(completada = !tarea.completada) else it
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
            }
        }
    }

    /**
     * Restablece los estados de éxito y error del diálogo tras la creación de una tarea.
     */
    fun afterCreateTask() {
        _registersucessful.value = false
        _errorDialog.value = null
    }

    /**
     * Crea una nueva tarea y la envía al servidor.
     * @param titulo Título de la tarea.
     * @param descripcion Descripción de la tarea.
     * @param usuarioid ID del usuario que creó la tarea.
     */
    fun crearTarea(titulo: String, descripcion: String, usuarioid: String) {
        viewModelScope.launch {
            try {
                val tareaInsertDTO = TareaInsertDTO(
                    titulo = titulo,
                    descripcion = descripcion,
                    usuarioId = usuarioid
                )

                val response = api.crearTarea(tareaInsertDTO)
                if (response.isSuccessful) {
                    _tareas.value += response.body()!!
                    _errorDialog.value = null
                    _registersucessful.value = true
                } else {
                    _registersucessful.value = false
                    _errorDialog.value = "No puedes asignar una tarea a otro usuario"
                }
            } catch (e: Exception) {
                _registersucessful.value = false
                _errorDialog.value = "Error de conexión"
            }
        }
    }

    /**
     * Elimina una tarea específica.
     * @param tareaId ID de la tarea a eliminar.
     */
    fun eliminarTarea(tareaId: String) {
        viewModelScope.launch {
            try {
                val response = api.eliminarTarea(tareaId)
                if (response.isSuccessful) {
                    _tareas.value = _tareas.value.filterNot { it._id == tareaId }
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
            }
        }
    }
}