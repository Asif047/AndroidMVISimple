package com.asif047.myapplication.domain.model

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val userId: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val image: String
)
