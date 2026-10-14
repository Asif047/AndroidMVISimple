package com.asif047.myapplication.presentation.login

import com.asif047.myapplication.domain.model.AuthToken

object LoginContract {
    data class State(
        val username: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val authToken: AuthToken? = null
    )

    sealed interface Event {
        data class OnUsernameChanged(val username: String) : Event
        data class OnPasswordChanged(val password: String) : Event
        data object OnLoginClicked : Event
        data object OnDismissError : Event
    }

    sealed interface Effect {
        data object NavigateToUserScreen : Effect
        data class ShowToast(val message: String) : Effect
    }
}
