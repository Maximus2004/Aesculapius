package com.example.aesculapius.ui.tests

import java.time.LocalDate

data class Test(
    val listOfQuestion: MutableList<Question>,
    val targetTime: LocalDate,
    val badResult: String,
    val mediumResult: String,
    val goodResult: String
)
