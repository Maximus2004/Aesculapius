package com.example.aesculapius.ui.therapy

data class TherapyUiState(
    val currentMorningMedicines: List<MedicineCard>,
    val currentEveningMedicines: List<MedicineCard>,
    val done: Int = 0,
    val amount: Int = 0,
    val progress: Float = 0.0f
)
