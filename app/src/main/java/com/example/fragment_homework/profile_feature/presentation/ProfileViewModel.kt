package com.example.fragment_homework.profile_feature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fragment_homework.core.data.storage.TempStorage
import com.example.fragment_homework.core.domain.models.User
import com.example.fragment_homework.profile_feature.domain.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val tempStorage: TempStorage
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState(events = listOf(), user = null))
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update { state ->
            state.copy(user = User(email = tempStorage.createdUserEmail.toString(), name = tempStorage.createdUserName.toString(), password = tempStorage.createdUserPassword.toString()))
        }
    }

     fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            tempStorage.isLoggedIn = false
        }
    }



    fun consumeEvent(event: ProfileUiState.Events) {
        _uiState.update { value ->
            value.copy() - event
        }
    }
}