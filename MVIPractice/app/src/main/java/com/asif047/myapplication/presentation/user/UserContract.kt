package com.asif047.myapplication.presentation.user

import com.asif047.myapplication.domain.model.User

object UserContract {
    data class State(
        val users: List<User> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    sealed interface Event {
        data object OnFetchUsers : Event
        data object OnLogoutClicked : Event
    }

    sealed interface Effect {
        data class ShowToast(val message: String) : Effect
        data object NavigateToLogin : Effect
    }
}
