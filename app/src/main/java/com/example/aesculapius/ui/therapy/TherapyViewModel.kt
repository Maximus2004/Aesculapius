package com.example.aesculapius.ui.therapy

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aesculapius.data.CurrentMedicineType
import com.example.aesculapius.database.AesculapiusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.boguszpawlowski.composecalendar.week.Week
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

// все корутины запускаются с помощью конкретного диспетчера, который указывает на поток или
// пул потоков, на котором запускается данная каорутина, а также в конкретном контексте,
// который определяет жизненный цикл корутины
@HiltViewModel
class TherapyViewModel @Inject constructor(private val aesculapiusRepository: AesculapiusRepository) : ViewModel() {

    /** [currentDate] - при изменении выбранного дня, меняется currentDate вместе с state, который отвечает за отображение
     * дня в календаре. При изменении currentDate внутри списка не происходит recomposition,
     * таким образом данная "уловка" позволяет не отрисовывать календарь дважды, а только получать
     * текущую дату, когда это необходимо для отображения лекарств */
    private val currentDate = mutableStateListOf(LocalDate.now())

    var currentLoadingState: MutableStateFlow<CurrentLoadingState> = MutableStateFlow(CurrentLoadingState.Loading)
    var generalLoadingState: MutableStateFlow<GeneralLoadingState> = MutableStateFlow(GeneralLoadingState.Success)

    private var _currentWeekDates = MutableStateFlow(Week.now())
    val currentWeekDates: StateFlow<Week> = _currentWeekDates

    private var _isWeek = MutableStateFlow(true)
    val isWeek: StateFlow<Boolean> = _isWeek

    init { updateCurrentDate(LocalDate.now()) }

    fun changeIsWeek(isWeek: Boolean) {
        _isWeek.value = isWeek
    }

    /** [updateCurrentDate] - при обновлении даты требует обновить список лекарств к употреблению
     * в это время (связано с работой с данными, поэтому выполняется в фоне, чтобы не блокировать ui) */
    fun updateCurrentDate(newDate: LocalDate): Boolean {
        viewModelScope.launch {
            currentLoadingState.value = CurrentLoadingState.Loading
            currentDate[0] = newDate

            var amountDone = 0
            val medicines = aesculapiusRepository.getMedicinesOnCurrentDate(newDate).toMutableList()
            val morningMedicines = mutableListOf<MedicineCard>()
            val eveningMedicines = mutableListOf<MedicineCard>()
            medicines.forEach { medicineWithDoses ->
                medicineWithDoses.doses.forEach { dose ->
                    if (dose.isAccepted && dose.date == newDate) amountDone++
                    if (dose.isMorning && dose.date == newDate)
                        morningMedicines.add(
                            MedicineCard(
                                id = medicineWithDoses.medicine.idMedicine,
                                dose = medicineWithDoses.medicine.dose,
                                frequency = dose.dosesAmount,
                                isAccepted = dose.isAccepted,
                                isSkipped = dose.isSkipped || dose.date.isBefore(LocalDate.now()),
                                name = medicineWithDoses.medicine.name,
                                undername = medicineWithDoses.medicine.undername,
                                startDate = medicineWithDoses.medicine.startDate,
                                endDate = medicineWithDoses.medicine.endDate,
                                medicineType = medicineWithDoses.medicine.medicineType,
                                doseId = dose.idDose,
                                fullFrequency = medicineWithDoses.medicine.frequency
                            )
                        )
                    else if (!dose.isMorning && dose.date == newDate)
                        eveningMedicines.add(
                            MedicineCard(
                                id = medicineWithDoses.medicine.idMedicine,
                                dose = medicineWithDoses.medicine.dose,
                                frequency = dose.dosesAmount,
                                isAccepted = dose.isAccepted,
                                isSkipped = dose.isSkipped || dose.date.isBefore(LocalDate.now()),
                                name = medicineWithDoses.medicine.name,
                                undername = medicineWithDoses.medicine.undername,
                                startDate = medicineWithDoses.medicine.startDate,
                                endDate = medicineWithDoses.medicine.endDate,
                                medicineType = medicineWithDoses.medicine.medicineType,
                                doseId = dose.idDose,
                                fullFrequency = medicineWithDoses.medicine.frequency
                            )
                        )
                }
            }

            val result = TherapyUiState(
                currentMorningMedicines = morningMedicines,
                currentEveningMedicines = eveningMedicines,
                done = amountDone,
                amount = morningMedicines.size + eveningMedicines.size,
                progress = amountDone.toFloat() / (morningMedicines.size + eveningMedicines.size)
            )
            currentLoadingState.value = CurrentLoadingState.Success(result)
        }
        return true
    }

    fun acceptMedicine(idDose: Int) {
        viewModelScope.launch {
            generalLoadingState.value = GeneralLoadingState.Loading
            aesculapiusRepository.acceptMedicine(idDose)
            updateCurrentDate(LocalDate.now())
            generalLoadingState.value = GeneralLoadingState.Success
        }
    }

    fun skipMedicine(idDose: Int) {
        viewModelScope.launch {
            generalLoadingState.value = GeneralLoadingState.Loading
            aesculapiusRepository.skipMedicine(idDose)
            updateCurrentDate(LocalDate.now())
            generalLoadingState.value = GeneralLoadingState.Success
        }
    }

    fun addMedicineItem(
        medicineType: CurrentMedicineType,
        name: String,
        undername: String,
        dose: String,
        frequency: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ) {
        viewModelScope.launch {
            generalLoadingState.value = GeneralLoadingState.Loading
            aesculapiusRepository.insertMedicineItem(medicineType, name, undername, dose, frequency, startDate, endDate)
            if (!currentDate.first().isBefore(LocalDate.now()))
                updateCurrentDate(getCurrentDate())
            generalLoadingState.value = GeneralLoadingState.Success
        }
    }

    fun updateMedicineItem(medicineId: Int, frequency: String, dose: String, medicineType: CurrentMedicineType, startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch {
            generalLoadingState.value = GeneralLoadingState.Loading
            aesculapiusRepository.updateMedicineItem(medicineId, frequency, dose, medicineType, startDate, endDate)
            updateCurrentDate(LocalDate.now())
            generalLoadingState.value = GeneralLoadingState.Success
        }
    }

    fun deleteMedicineItem(medicineId: Int) {
        viewModelScope.launch {
            generalLoadingState.value = GeneralLoadingState.Loading
            aesculapiusRepository.deleteMedicineItem(medicineId)
            updateCurrentDate(LocalDate.now())
            generalLoadingState.value = GeneralLoadingState.Success
        }
    }

    /** [getWeekDates] - отображает неделю, на которой находится пользователь
     * (слишком быстро, чтобы выносить в фон) */
    fun getWeekDates(currentDate: LocalDate) {
        val weekDates = ArrayList<LocalDate>()
        var startOfWeek = currentDate
        while (startOfWeek.dayOfWeek != DayOfWeek.MONDAY) {
            startOfWeek = startOfWeek.minusDays(1)
        }
        for (i in 0 until 7) {
            weekDates.add(startOfWeek.plusDays(i.toLong()))
        }
        _currentWeekDates.update { Week(weekDates) }
    }

    /** [getAmountNotAcceptedMedicines] - служит для отображения индикторов под датами
     * (вызывается из LaunchedEffect) */
    suspend fun getAmountNotAcceptedMedicines(date: LocalDate): Int = viewModelScope.async {
        aesculapiusRepository.getAmountNotAcceptedMedicines(date)
    }.await()

    fun getCurrentDate(): LocalDate {
        return currentDate.first()
    }
}

sealed interface CurrentLoadingState {
    data class Success(val therapyuiState: TherapyUiState) : CurrentLoadingState
    object Loading : CurrentLoadingState
}

sealed interface GeneralLoadingState {
    object Success : GeneralLoadingState
    object Loading : GeneralLoadingState
}