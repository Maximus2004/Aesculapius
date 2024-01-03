package com.example.aesculapius.ui.therapy

import android.media.Image
import androidx.annotation.DrawableRes
import java.time.LocalDate

data class MedicineItem(
    @DrawableRes val image: Int,
    val name: String,
    val undername: String,
    val dose: String,
    val frequency: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)
