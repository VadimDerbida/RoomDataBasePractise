package com.example.fragment_homework.auth_feature.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class SignUpViewModel @Inject constructor(
    private val tempStorage: TempStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState(loading = false, successSignUp = null, events = listOf()))
    val uiState = _uiState.asStateFlow()

    init {
        if (tempStorage.isLoggedIn) {
            _uiState.update { value ->
                value.copy() + SignUpUiState.Events.NavigationToProfile()
            }
        }
    }

    fun signUp(email: String, password: String, name: String) {

        _uiState.update { value ->
            value.copy(loading = true)
        }

        val user = User(email = email, name = name, password = password)

        var error = SignUpUiState.SignUpError.NONE
        if (user.email.isBlank()) {
            error = SignUpUiState.SignUpError.EMPTY_FIELDS
        }
        if (user.name.isBlank()) {
            error = SignUpUiState.SignUpError.EMPTY_FIELDS
        }

        if (user.password.isBlank()) {
            error = SignUpUiState.SignUpError.EMPTY_FIELDS
        }

        when (error) {
            SignUpUiState.SignUpError.EMPTY_FIELDS -> {
                viewModelScope.launch(Dispatchers.IO) {
                    delay(1000)
                    _uiState.update { value ->
                        value.copy(loading = false) + SignUpUiState.Events.ShowError("Fields cannot be empty")
                    }
                    error = SignUpUiState.SignUpError.NONE
                }
            }

            SignUpUiState.SignUpError.NONE -> {
                viewModelScope.launch(Dispatchers.IO) {
                    delay(1000)
                    tempStorage.createdUserEmail = user.email
                    tempStorage.createdUserPassword = user.password
                    tempStorage.createdUserName = user.name
                    tempStorage.isLoggedIn = true
                    _uiState.update { value ->
                        value.copy(loading = false) + SignUpUiState.Events.NavigationToProfile()
                    }
                }
            }
        }
    }

    fun consumeEvent(event: SignUpUiState.Events) {
        _uiState.update { value ->
            value.copy() - event
        }
    }
}

