package com.asif047.myapplication.domain.usecase

import com.asif047.myapplication.domain.model.AuthToken
import com.asif047.myapplication.domain.repository.AuthRepository
import com.asif047.myapplication.util.Resource
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Resource<AuthToken> {
        if (username.isBlank()) {
            return Resource.Error(message = "Username cannot be empty")
        }
        if (password.isBlank()) {
            return Resource.Error(message = "Password cannot be empty")
        }
        return authRepository.login(username = username, password = password)
    }
}
