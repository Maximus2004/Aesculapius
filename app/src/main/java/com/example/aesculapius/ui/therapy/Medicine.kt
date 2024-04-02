package com.example.aesculapius.ui.therapy

import androidx.annotation.DrawableRes
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

/** [Medicine] для выбора нового препарата */
data class Medicine(
    @DrawableRes val image: Int,
    val name: String,
    val undername: String,
    val doses: List<String>,
    val frequency: List<String>
)
