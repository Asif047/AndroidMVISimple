package com.asif047.myapplication.data.repository

import com.asif047.myapplication.config.IoDispatcher
import com.asif047.myapplication.data.local.TokenManager
import com.asif047.myapplication.data.model.LoginRequest
import com.asif047.myapplication.data.remote.AuthApiService
import com.asif047.myapplication.domain.model.AuthToken
import com.asif047.myapplication.domain.repository.AuthRepository
import com.asif047.myapplication.util.NetworkErrorResponse
import com.asif047.myapplication.util.Resource
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val tokenManager: TokenManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthRepository {

    override suspend fun login(username: String, password: String): Resource<AuthToken> =
        withContext(ioDispatcher) {
            try {
                val response = authApiService.login(LoginRequest(username = username, password = password))
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    tokenManager.saveAccessToken(loginResponse.accessToken)
                    tokenManager.saveRefreshToken(loginResponse.refreshToken)

                    val authToken = AuthToken(
                        accessToken = loginResponse.accessToken,
                        refreshToken = loginResponse.refreshToken,
                        userId = loginResponse.id,
                        username = loginResponse.username,
                        email = loginResponse.email,
                        firstName = loginResponse.firstName,
                        lastName = loginResponse.lastName,
                        gender = loginResponse.gender,
                        image = loginResponse.image
                    )
                    Resource.Success(authToken)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val parsedError = parseError(errorBody, response.code())
                    Resource.Error(
                        message = parsedError.message ?: "Login failed",
                        status = parsedError.status ?: response.code(),
                        data = null
                    )
                }
            } catch (e: Exception) {
                Resource.Error(
                    message = e.localizedMessage ?: "An unexpected network error occurred",
                    status = -1
                )
            }
        }

    override suspend fun getSavedAccessToken(): String? = withContext(ioDispatcher) {
        tokenManager.getAccessToken()
    }

    override suspend fun logout() = withContext(ioDispatcher) {
        tokenManager.clearTokens()
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
