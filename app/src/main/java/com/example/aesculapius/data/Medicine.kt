package com.example.aesculapius.data

import androidx.annotation.DrawableRes
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime

// информация о каждом лекарстве
data class Medicine(
    @DrawableRes val image: Int,
    val name: String,
    val dose: Int,
    val time: LocalTime,
    val startDate: LocalDate,
    val endDate: LocalDate
)
