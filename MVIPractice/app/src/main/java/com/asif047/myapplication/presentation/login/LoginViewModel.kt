package com.asif047.myapplication.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asif047.myapplication.domain.usecase.LoginUseCase
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
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginContract.State())
    val state: StateFlow<LoginContract.State> = _state.asStateFlow()

    private val _effect = Channel<LoginContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: LoginContract.Event) {
        when (event) {
            is LoginContract.Event.OnUsernameChanged -> {
                _state.update { it.copy(username = event.username, errorMessage = null) }
            }
            is LoginContract.Event.OnPasswordChanged -> {
                _state.update { it.copy(password = event.password, errorMessage = null) }
            }
            is LoginContract.Event.OnLoginClicked -> {
                login()
            }
            is LoginContract.Event.OnDismissError -> {
                _state.update { it.copy(errorMessage = null) }
            }
        }
    }

    private fun login() {
        val currentState = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = loginUseCase(currentState.username, currentState.password)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            authToken = result.data,
                            errorMessage = null
                        )
                    }
                    _effect.send(LoginContract.Effect.NavigateToUserScreen)
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Login failed"
                        )
                    }
                    _effect.send(LoginContract.Effect.ShowToast(result.message ?: "Login failed"))
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }
}
