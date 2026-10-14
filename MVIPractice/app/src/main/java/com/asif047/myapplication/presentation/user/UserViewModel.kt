package com.asif047.myapplication.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asif047.myapplication.domain.repository.AuthRepository
import com.asif047.myapplication.domain.usecase.GetUsersUseCase
import com.asif047.myapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(UserContract.State())
    val state: StateFlow<UserContract.State> = _state.asStateFlow()

    private val _effect = Channel<UserContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        fetchUsers()
    }

    fun onEvent(event: UserContract.Event) {
        when (event) {
            is UserContract.Event.OnFetchUsers -> {
                fetchUsers()
            }
            is UserContract.Event.OnLogoutClicked -> {
                logout()
            }
        }
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = getUsersUseCase()) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            users = result.data ?: emptyList(),
                            errorMessage = null
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Failed to fetch users"
                        )
                    }
                    _effect.send(UserContract.Effect.ShowToast(result.message ?: "Failed to load users"))
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _effect.send(UserContract.Effect.NavigateToLogin)
        }
    }
}
