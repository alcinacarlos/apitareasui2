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

interface ApiService {
    @POST("/usuarios/login")
    suspend fun login(@Body request: LoginUsuarioDTO): Response<LoginResponse>

    @POST("/usuarios/register")
    suspend fun register(@Body request: UsuarioRegisterDTO): Response<Unit>

    @GET("/tareas")
    suspend fun obtenerTareas(): Response<List<Tarea>>

    @POST("/tareas")
    suspend fun crearTarea(@Body tarea: TareaInsertDTO): Response<Tarea>

    @PUT("/tareas/{tareaId}/completar")
    suspend fun marcarComoHecha(@Path("tareaId") tareaId: String): Response<Unit>

    @DELETE("/tareas/{tareaId}")
    suspend fun eliminarTarea(@Path("tareaId") tareaId: String): Response<Unit>
}