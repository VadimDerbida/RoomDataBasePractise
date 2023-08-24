package com.example.fragment_homework.auth_feature.domain.state

data class LogInUiState(
    val loading: Boolean,
    val events: List<Events> = listOf()
) {

    sealed class Events {
        class NavigationToProfile(): Events()
        data class ShowError(val message: String): Events()
    }

    enum class LogInError{
        EMPTY_FIELDS,
        WRONG_FIELDS,
        NONE
    }

    operator fun plus(event: Events) = copy(events = events + event)

    operator fun minus(event: Events) = copy(events = events - event)
}