package com.example.aesculapius.ui.therapy

import com.example.aesculapius.data.Medicine
import com.example.aesculapius.data.medicines
import java.time.LocalDate

data class TherapyUiState(
    val currentActiveMedicines: MutableList<Medicine> = medicines[LocalDate.now().month.value - 1][LocalDate.now().dayOfMonth - 1].active,
    val currentEndedMedicines: MutableList<Medicine> = medicines[LocalDate.now().month.value - 1][LocalDate.now().dayOfMonth - 1].ended,
    val done: Int = 0,
    val amount: Int = 0,
    val progress: Float = 0.0f
)
