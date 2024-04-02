package com.example.aesculapius.ui.tests

import androidx.annotation.StringRes

data class Question(
    @StringRes
    val questionText: Int,
    val answersList: MutableList<Int>
)
