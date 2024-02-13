package com.example.aesculapius.ui.therapy

import com.example.aesculapius.data.CurrentMedicineType
import java.time.LocalDate

// для отображения на экране терапии
data class MedicineCard(
    val id: Int,
    val name: String,
    val undername: String,
    val dose: String,
    val frequency: String,
    val isSkipped: Boolean,
    val isAccepted: Boolean,
    val medicineType: CurrentMedicineType,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val doseId: Int,
    val fullFrequency: String
)
