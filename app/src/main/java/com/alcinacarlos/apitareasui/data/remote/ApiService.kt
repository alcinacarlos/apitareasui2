package com.alcinacarlos.apitareasui.data.remote

import com.alcinacarlos.apitareasui.data.model.LoginResponse
import com.alcinacarlos.apitareasui.dto.LoginUsuarioDTO
import com.alcinacarlos.apitareasui.dto.UsuarioRegisterDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/usuarios/login")
    suspend fun login(@Body request: LoginUsuarioDTO): LoginResponse

    @POST("/usuarios/register")
    suspend fun register(@Body request: UsuarioRegisterDTO): Response<Unit>
}