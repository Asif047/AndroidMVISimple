package com.asif047.myapplication.domain.usecase

import com.asif047.myapplication.domain.model.User
import com.asif047.myapplication.domain.repository.UserRepository
import com.asif047.myapplication.util.Resource
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Resource<List<User>> {
        return userRepository.getUsers()
    }
}
