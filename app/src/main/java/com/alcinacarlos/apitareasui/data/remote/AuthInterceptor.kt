package com.alcinacarlos.apitareasui.data.remote

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor de autenticación que añade el token de autorización a cada solicitud HTTP.
 *
 * @property token Token de autenticación que se incluirá en las cabeceras de las solicitudes.
 */
class AuthInterceptor(private val token: String) : Interceptor {

    /**
     * Intercepta la solicitud y añade el encabezado de autorización con el token Bearer.
     *
     * @param chain Cadena de interceptores que contiene la solicitud en proceso.
     * @return La respuesta resultante después de agregar el encabezado de autorización.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}
