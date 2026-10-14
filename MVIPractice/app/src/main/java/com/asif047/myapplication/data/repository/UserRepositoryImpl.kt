package com.asif047.myapplication.data.repository

import com.asif047.myapplication.data.remote.UserApiService
import com.asif047.myapplication.domain.model.User
import com.asif047.myapplication.domain.repository.UserRepository
import com.asif047.myapplication.util.NetworkErrorResponse
import com.asif047.myapplication.util.Resource
import com.google.gson.Gson
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserRepository {

    override suspend fun getUsers(): Resource<List<User>> {
        return try {
            val response = userApiService.getUsers()
            if (response.isSuccessful && response.body() != null) {
                val domainUsers = response.body()!!.map { dto ->
                    User(
                        id = dto.id,
                        name = dto.name,
                        username = dto.username,
                        email = dto.email,
                        phone = dto.phone,
                        website = dto.website,
                        companyName = dto.company.name,
                        city = dto.address.city
                    )
                }
                Resource.Success(domainUsers)
            } else {
                val errorBody = response.errorBody()?.string()
                val parsedError = parseError(errorBody, response.code())
                Resource.Error(
                    message = parsedError.message ?: "Failed to fetch users",
                    status = parsedError.status ?: response.code()
                )
            }
        } catch (e: Exception) {
            Resource.Error(
                message = e.localizedMessage ?: "An unexpected network error occurred",
                status = -1
            )
        }
    }

    private fun parseError(errorJson: String?, defaultCode: Int): NetworkErrorResponse {
        return try {
            if (!errorJson.isNullOrEmpty()) {
                Gson().fromJson(errorJson, NetworkErrorResponse::class.java)
            } else {
                NetworkErrorResponse(status = defaultCode, message = "Unknown error")
            }
        } catch (e: Exception) {
            NetworkErrorResponse(status = defaultCode, message = "Failed to parse error response")
        }
    }
}
