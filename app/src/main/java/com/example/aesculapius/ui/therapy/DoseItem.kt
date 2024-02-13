package com.example.aesculapius.ui.therapy

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "dose_items")
data class DoseItem(
    @PrimaryKey(autoGenerate = true)
    val idDose: Int,
    val dosesAmount: String,
    val isMorning: Boolean,
    val date: LocalDate,
    val isSkipped: Boolean,
    val isAccepted: Boolean,
    val medicineId: Int
)
