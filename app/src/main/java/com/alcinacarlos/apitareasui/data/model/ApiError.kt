package com.alcinacarlos.apitareasui.data.model

import com.google.gson.annotations.SerializedName

data class ApiError(
    @SerializedName("message") val message: String,
    @SerializedName("uri") val uri: String
)
