package com.asif047.myapplication.data.remote

import com.asif047.myapplication.data.model.UserDto
import retrofit2.Response
import retrofit2.http.GET

interface UserApiService {
    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>
}
