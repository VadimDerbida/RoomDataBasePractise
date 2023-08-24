package com.example.fragment_homework.auth_feature.domain.state

import com.example.fragment_homework.core.domain.models.User

data class SignUpUiState(
    val loading: Boolean,
    val successSignUp: User? = null,
    val events: List<Events> = listOf()
) {

    sealed class Events {
        class NavigationToProfile(): Events()
        data class ShowError(val message: String): Events()
    }

    enum class SignUpError{
        EMPTY_FIELDS,
        NONE
    }

    operator fun plus(event: Events) = copy(events = events + event)

    operator fun minus(event: Events) = copy(events = events - event)
}
