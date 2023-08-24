package com.example.fragment_homework.profile_feature.domain

import com.example.fragment_homework.core.domain.models.User

data class ProfileUiState(
    val user: User?,
    val events: List<Events> = listOf())
{
    sealed class Events {
        data class NavigationToSignUp(val user: User?): Events()
    }

    operator fun plus(event: Events) = copy(events = events + event)

    operator fun minus(event: Events) = copy(events = events - event)
}
