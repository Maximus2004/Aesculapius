package com.example.aesculapius.ui.therapy

import com.example.aesculapius.data.CurrentMedicineType
import java.time.LocalDate

/** [MedicineCard] для отображения на экране терапии (класс в который стекаются данные из доз и препаратов) */
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
