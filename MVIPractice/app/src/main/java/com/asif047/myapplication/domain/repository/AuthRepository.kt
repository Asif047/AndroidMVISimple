package com.asif047.myapplication.domain.repository

import com.asif047.myapplication.domain.model.AuthToken
import com.asif047.myapplication.util.Resource

interface AuthRepository {
    suspend fun login(username: String, password: String): Resource<AuthToken>
    suspend fun getSavedAccessToken(): String?
    suspend fun logout()
}
