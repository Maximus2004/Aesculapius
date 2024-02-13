package com.example.aesculapius.ui.therapy

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aesculapius.data.CurrentMedicineType
import java.time.LocalDate

// для отображения в БД
@Entity(tableName = "medicines_items")
data class MedicineItem(
    @PrimaryKey(autoGenerate = false)
    val idMedicine: Int,
    val medicineType: CurrentMedicineType,
    val name: String,
    val undername: String,
    val dose: String,
    val frequency: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)
