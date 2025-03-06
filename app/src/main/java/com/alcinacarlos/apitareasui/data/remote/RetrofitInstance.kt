package com.alcinacarlos.apitareasui.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api-tareas-1vo7.onrender.com"
    //private const val BASE_URL = "http://localhost:8080"

    private var token = ""

    fun changeToken(newToken: String?){
        token = newToken ?: ""
    }

    fun getInstance(): ApiService {
        if (token.isNotBlank()){
            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(token))
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }else{
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }



    }
}