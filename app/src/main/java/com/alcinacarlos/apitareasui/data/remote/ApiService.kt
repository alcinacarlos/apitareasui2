package com.alcinacarlos.apitareasui.data.remote

import com.alcinacarlos.apitareasui.data.model.LoginResponse
import com.alcinacarlos.apitareasui.data.model.Tarea
import com.alcinacarlos.apitareasui.dto.LoginUsuarioDTO
import com.alcinacarlos.apitareasui.dto.TareaInsertDTO
import com.alcinacarlos.apitareasui.dto.UsuarioRegisterDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Interfaz que define los servicios de la API para la gestión de usuarios y tareas.
 */
interface ApiService {

    /**
     * Inicia sesión con las credenciales proporcionadas.
     *
     * @param request Objeto con los datos del usuario (usuario y contraseña).
     * @return Respuesta con el token de autenticación si es exitoso.
     */
    @POST("/usuarios/login")
    suspend fun login(@Body request: LoginUsuarioDTO): Response<LoginResponse>

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param request Objeto con los datos del usuario a registrar.
     * @return Respuesta sin cuerpo (código de estado indica el éxito o fallo).
     */
    @POST("/usuarios/register")
    suspend fun register(@Body request: UsuarioRegisterDTO): Response<Unit>

    /**
     * Obtiene la lista de tareas del usuario autenticado.
     *
     * @return Respuesta con la lista de tareas o un error en caso de fallo.
     */
    @GET("/tareas")
    suspend fun obtenerTareas(): Response<List<Tarea>>

    /**
     * Crea una nueva tarea en el sistema.
     *
     * @param tarea Objeto con los datos de la tarea a insertar.
     * @return Respuesta con la tarea creada o un error en caso de fallo.
     */
    @POST("/tareas")
    suspend fun crearTarea(@Body tarea: TareaInsertDTO): Response<Tarea>

    /**
     * Marca una tarea como completada.
     *
     * @param tareaId ID de la tarea que se va a marcar como completada.
     * @return Respuesta sin cuerpo (código de estado indica éxito o fallo).
     */
    @PUT("/tareas/{tareaId}/completar")
    suspend fun marcarComoHecha(@Path("tareaId") tareaId: String): Response<Unit>

    /**
     * Elimina una tarea del sistema.
     *
     * @param tareaId ID de la tarea que se va a eliminar.
     * @return Respuesta sin cuerpo (código de estado indica éxito o fallo).
     */
    @DELETE("/tareas/{tareaId}")
    suspend fun eliminarTarea(@Path("tareaId") tareaId: String): Response<Unit>
}
