package com.alcinacarlos.apitareasui.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto singleton que proporciona una instancia de Retrofit para realizar solicitudes HTTP a la API.
 * Permite cambiar el token de autenticación dinámicamente.
 */
object RetrofitInstance {
    private const val BASE_URL = "https://api-tareas-1vo7.onrender.com"
    //private const val BASE_URL = "http://localhost:8080"

    private var token = ""

    /**
     * Cambia el token de autenticación utilizado en las solicitudes.
     *
     * @param newToken Nuevo token de autenticación. Si es `null`, se establece una cadena vacía.
     */
    fun changeToken(newToken: String?) {
        token = newToken ?: ""
    }

    /**
     * Obtiene una instancia de [ApiService] configurada con o sin autenticación según la presencia de un token.
     *
     * @return Instancia de [ApiService] lista para hacer peticiones a la API.
     */
    fun getInstance(): ApiService {
        return if (token.isNotBlank()) {
            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(token))
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            retrofit.create(ApiService::class.java)
        } else {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(ApiService::class.java)
        }
    }
}
