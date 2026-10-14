package com.asif047.myapplication.domain.repository

import com.asif047.myapplication.domain.model.User
import com.asif047.myapplication.util.Resource

interface UserRepository {
    suspend fun getUsers(): Resource<List<User>>
}
