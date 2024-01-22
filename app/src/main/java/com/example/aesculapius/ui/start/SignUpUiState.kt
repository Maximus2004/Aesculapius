package com.example.aesculapius.ui.start

import java.time.LocalDate
import java.time.LocalTime

data class SignUpUiState(
    val name: String = "",
    val surname: String = "",
    val patronymic: String = "",
    val birthday: LocalDate = LocalDate.now(),
    val height: String = "",
    val weight: String = "",
)
