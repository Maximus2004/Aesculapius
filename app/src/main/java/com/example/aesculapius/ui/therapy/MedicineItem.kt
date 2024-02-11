package com.example.aesculapius.ui.therapy

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

// 00 (0) - оба активны
// 01 (1) - утром активен, вечером неактивен
// 10 (2) - утром неактивен, вечером активен
// 11 (3) - оба неактивны
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
    val isAccepted: MutableList<Int>,
    val isSkipped: MutableList<Int>,
    val realStartDate: LocalDate
)
