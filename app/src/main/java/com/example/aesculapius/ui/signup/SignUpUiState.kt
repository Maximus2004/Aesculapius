package com.example.aesculapius.ui.signup

import java.time.LocalDate
import java.time.LocalDateTime

data class SignUpUiState(
    val id: String? = null,
    val userRegisterDate: LocalDate = LocalDate.now(),
    val name: String = "",
    val surname: String = "",
    val patronymic: String = "",
    val birthday: LocalDate = LocalDate.now(),
    val height: String = "",
    val weight: String = "",
    val morningReminder: LocalDateTime = LocalDateTime.now(),
    val eveningReminder: LocalDateTime = LocalDateTime.now(),
    val astTestDate: String = "",
    val recommendationTestDate: String = ""
)
