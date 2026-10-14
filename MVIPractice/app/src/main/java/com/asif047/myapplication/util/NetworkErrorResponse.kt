package com.asif047.myapplication.util

import com.google.gson.annotations.SerializedName

data class NetworkErrorResponse(
    @SerializedName("data")
    val data: Any? = null,
    @SerializedName("status")
    val status: Int? = null,
    @SerializedName("message")
    val message: String? = null
)
