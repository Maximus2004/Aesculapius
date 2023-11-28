package com.example.aesculapius.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime

class SignUpViewModel : ViewModel() {
    // backing property
    private val _uiStateSignUp = MutableStateFlow(SignUpUiState())
    val uiStateSingUp: StateFlow<SignUpUiState> = _uiStateSignUp

    fun onNameChanged(name: String) {
        _uiStateSignUp.update {
            it.copy(name = name)
        }
    }
    fun onSurnameChanged(surname: String) {
        _uiStateSignUp.update {
            it.copy(surname = surname)
        }
    }
    fun onChangedPatronymic(patronymic: String) {
        _uiStateSignUp.update {
            it.copy(patronymic = patronymic)
        }
    }
    fun onHeightChanged(height: String) {
        _uiStateSignUp.update {
            it.copy(height = height)
        }
    }
    fun onWeightChanged(weight: String) {
        _uiStateSignUp.update {
            it.copy(weight = weight)
        }
    }
    fun onDateChanged(date: LocalDate) {
        _uiStateSignUp.update {
            it.copy(birthday = date)
        }
    }
    fun onMorningTimeChanged(time: LocalTime) {
        _uiStateSignUp.update {
            it.copy(reminderMorning = time)
        }
    }
    fun onEveningTimeChanged(time: LocalTime) {
        _uiStateSignUp.update {
            it.copy(reminderEvening = time)
        }
    }
}