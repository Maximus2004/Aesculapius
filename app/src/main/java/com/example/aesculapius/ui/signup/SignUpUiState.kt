package com.example.aesculapius.ui.signup

import java.time.LocalDate

data class SignUpUiState(
    val name: String = "",
    val surname: String = "",
    val patronymic: String = "",
    val birthday: LocalDate = LocalDate.now(),
    val height: String = "",
    val weight: String = "",
)
