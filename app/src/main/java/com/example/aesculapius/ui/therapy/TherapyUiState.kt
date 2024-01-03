package com.example.aesculapius.ui.therapy

data class TherapyUiState(
    val currentActiveMedicines: MutableList<MedicineItem>,
    val currentEndedMedicines: MutableList<MedicineItem>,
    val done: Int = 0,
    val amount: Int = 0,
    val progress: Float = 0.0f
)
