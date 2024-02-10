package com.example.aesculapius.ui.therapy

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "medicines_items")
data class MedicineItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @DrawableRes
    val image: Int,
    val medicineType: String,
    val name: String,
    val undername: String,
    val dose: String,
    val frequency: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val isAccepted: Boolean,
    val isSkipped: Boolean
)
