package com.asif047.myapplication.data.remote

import com.asif047.myapplication.data.model.LoginRequest
import com.asif047.myapplication.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
