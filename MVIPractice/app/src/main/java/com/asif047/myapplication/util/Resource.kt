package com.asif047.myapplication.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val status: Int? = null
) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(
        message: String,
        status: Int? = null,
        data: T? = null
    ) : Resource<T>(data = data, message = message, status = status)

    class Loading<T>(data: T? = null) : Resource<T>(data = data)
}
