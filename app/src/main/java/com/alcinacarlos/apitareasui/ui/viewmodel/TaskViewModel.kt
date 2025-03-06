package com.alcinacarlos.apitareasui.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alcinacarlos.apitareasui.data.model.Tarea
import com.alcinacarlos.apitareasui.data.remote.RetrofitInstance
import com.alcinacarlos.apitareasui.dto.TareaInsertDTO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel() : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    private val api = RetrofitInstance.getInstance()

    private val _tareas = MutableStateFlow<List<Tarea>>(emptyList())
    val tareas: StateFlow<List<Tarea>> = _tareas

    init {
        viewModelScope.launch {
            obtenerTareas()
        }
    }
    fun obtenerTareas(){
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

    fun marcarComoHecha(tarea: Tarea) {
        viewModelScope.launch {
            try {
                val response = api.marcarComoHecha(tarea._id!!)
                if (response.isSuccessful){
                    // Actualizar la lista de tareas en local
                    _tareas.value = _tareas.value.map {
                        if (it._id == tarea._id) it.copy(completada = true) else it
                    }
                }

            } catch (e: Exception) {
                _error.value = "Error de conexión"
            }
        }
    }

    fun crearTarea(titulo: String, descripcion: String, usuarioid: String) {
        viewModelScope.launch {
            try {
                val tareaInsertDTO = TareaInsertDTO(
                    titulo = titulo,
                    descripcion = descripcion,
                    usuarioId = usuarioid
                )

                val nuevaTarea = api.crearTarea(tareaInsertDTO)
                if (nuevaTarea.isSuccessful){
                    _tareas.value += nuevaTarea.body()!!
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
            }
        }
    }

    fun eliminarTarea(tareaId: String) {
        viewModelScope.launch {
            try {
                val response = api.eliminarTarea(tareaId)
                if (response.isSuccessful){
                    _tareas.value = _tareas.value.filterNot { it._id == tareaId }
                }

            } catch (e: Exception) {
                _error.value = "Error de conexión"
            }
        }
    }

}
