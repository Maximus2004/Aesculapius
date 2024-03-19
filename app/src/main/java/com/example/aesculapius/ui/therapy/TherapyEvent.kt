package com.example.aesculapius.ui.therapy

import com.example.aesculapius.data.CurrentMedicineType
import java.time.LocalDate

sealed interface TherapyEvent {
    data class OnAcceptMedicine(val idDose: Int): TherapyEvent
    data class OnSkipMedicine(val idDose: Int): TherapyEvent
    data class OnChangeIsWeek(val isWeek: Boolean): TherapyEvent
    data class OnAddMedicineItem(
        val medicineType: CurrentMedicineType,
        val name: String,
        val undername: String,
        val dose: String,
        val frequency: String,
        val startDate: LocalDate,
        val endDate: LocalDate
    ): TherapyEvent
    data class OnUpdateMedicineItem(
        val medicineId: Int,
        val frequency: String,
        val dose: String,
        val medicineType: CurrentMedicineType,
        val startDate: LocalDate,
        val endDate: LocalDate
    ): TherapyEvent
    data class OnDeleteMedicineItem(val medicineId: Int): TherapyEvent
    data class OnGetWeekDates(val currentDate: LocalDate): TherapyEvent
}