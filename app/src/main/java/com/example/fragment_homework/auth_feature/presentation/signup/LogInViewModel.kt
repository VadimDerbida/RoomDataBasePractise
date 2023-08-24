package com.example.fragment_homework.auth_feature.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragment_homework.auth_feature.domain.state.LogInUiState
import com.example.fragment_homework.auth_feature.domain.state.SignUpUiState
import com.example.fragment_homework.core.data.storage.TempStorage
import com.example.fragment_homework.core.domain.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class LogInViewModel @Inject constructor(
    private val tempStorage: TempStorage
) : ViewModel() {
    private val _uiState = MutableStateFlow(LogInUiState(loading = false, events = listOf()))
    val uiState = _uiState.asStateFlow()

    init {
        if (tempStorage.isLoggedIn) {
            _uiState.update { value ->
                value.copy() + LogInUiState.Events.NavigationToProfile()
            }
        }
    }

    fun logIn(email: String, password: String, name: String) {
        _uiState.update { value ->
            value.copy(loading = true)
        }
        val user = User(email = email, password = password, name = name)

        var error: LogInUiState.LogInError
        error = when {
            user.email.isBlank() -> LogInUiState.LogInError.EMPTY_FIELDS
            user.password.isBlank() -> LogInUiState.LogInError.EMPTY_FIELDS
            user.email != tempStorage.createdUserEmail.toString() || user.password != tempStorage.createdUserPassword.toString() -> LogInUiState.LogInError.WRONG_FIELDS
            else -> LogInUiState.LogInError.NONE
        }

        when (error) {
            LogInUiState.LogInError.EMPTY_FIELDS -> {
                viewModelScope.launch(Dispatchers.IO) {
                    delay(1000)
                    _uiState.update { value ->
                        value.copy(loading = false) + LogInUiState.Events.ShowError("Fields cannot be empty")
                    }
                    error = LogInUiState.LogInError.NONE
                }
            }
            LogInUiState.LogInError.WRONG_FIELDS -> {
                viewModelScope.launch(Dispatchers.IO) {
                    delay(1000)
                    _uiState.update { value ->
                        value.copy(loading = false) + LogInUiState.Events.ShowError("Wrong email or password")
                    }
                    error = LogInUiState.LogInError.NONE
                }
            }

            LogInUiState.LogInError.NONE -> {
                viewModelScope.launch(Dispatchers.IO) {
                    delay(1000)
                    tempStorage.isLoggedIn = true
                    _uiState.update { value ->
                        value.copy(loading = false) + LogInUiState.Events.NavigationToProfile()
                    }
                }
            }
        }
    }
    fun consumeEvent(event: LogInUiState.Events) {
        _uiState.update { value ->
            value.copy() - event
        }
    }
}





