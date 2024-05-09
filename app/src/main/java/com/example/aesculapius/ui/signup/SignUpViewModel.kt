package com.example.aesculapius.ui.signup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SignUpViewModel : ViewModel() {
    private val _uiStateSignUp = MutableStateFlow(SignUpUiState())
    val uiStateSignUp: StateFlow<SignUpUiState> = _uiStateSignUp

    fun onEvent(event: SignUpEvent) {
        when(event) {
            is SignUpEvent.OnMorningReminderChanged -> {
                _uiStateSignUp.update {
                    it.copy(morningReminder = event.morningReminder)
                }
            }
            is SignUpEvent.OnEveningReminderChanged -> {
                _uiStateSignUp.update {
                    it.copy(eveningReminder = event.eveningReminder)
                }
            }
            is SignUpEvent.OnNameChanged -> {
                _uiStateSignUp.update {
                    it.copy(name = event.name)
                }
            }
            is SignUpEvent.OnSurnameChanged -> {
                _uiStateSignUp.update {
                    it.copy(surname = event.surname)
                }
            }
            is SignUpEvent.OnPatronymicChanged -> {
                _uiStateSignUp.update {
                    it.copy(patronymic = event.patronymic)
                }
            }
            is SignUpEvent.OnHeightChanged -> {
                _uiStateSignUp.update {
                    it.copy(height = event.height)
                }
            }
            is SignUpEvent.OnWeightChanged -> {
                _uiStateSignUp.update {
                    it.copy(weight = event.weight)
                }
            }
            is SignUpEvent.OnBirthdayChanged -> {
                _uiStateSignUp.update {
                    it.copy(birthday = event.birthday)
                }
            }
        }
    }
}