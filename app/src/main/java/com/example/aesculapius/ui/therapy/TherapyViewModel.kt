package com.example.aesculapius.ui.therapy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.boguszpawlowski.composecalendar.week.Week
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

// все корутины запускаются с помощью конкретного диспетчера, который казывает на поток или
// пул потоков, на котором запускается данная каорутина, а также в конкретном контексте,
// который определяет жизненный цикл корутины
class TherapyViewModel : ViewModel() {
    // при изменении выбранного дня, меняется currentDate вместе с state, который отвечает за отображение
    // дня в календаре. при изменении currentDate внутри списка не происходит recomposition
    // таким образом данная "уловка" позволяет не отрисовывать календарь дважды, а только получать
    // текущую дату, когда это необходимо для отображения лекарств
    val currentDate = mutableStateListOf(LocalDate.now())

    var currentLoadingState: CurrentLoadingState by mutableStateOf(CurrentLoadingState.Loading)
        private set

    // нужно будет вынести в Room
    private var _listUserMedicines = MutableStateFlow(mutableListOf<MedicineItem>())

    private var _currentWeekDates = MutableStateFlow(Week.now())
    val currentWeekDates: StateFlow<Week> = _currentWeekDates

    init {
        updateCurrentDate(LocalDate.now())
    }

    // при обновлении даты требует обновить список лекарств к употреблению в это время
    // (связано с работой с данными, поэтому выполняется в фоне, чтобы не блокировать ui)
    fun updateCurrentDate(newDate: LocalDate): Boolean {
        viewModelScope.launch {
            currentLoadingState = CurrentLoadingState.Loading
            currentDate[0] = newDate
            val activeTemp = _listUserMedicines.value.filter {
                it.startDate == newDate || (newDate.isAfter(it.startDate) && newDate.isBefore(it.endDate))
            }.toMutableList()

            // пока пустой список, так как не добавлена система отметки о завершении приёма лекарства
            val endedTemp = mutableListOf<MedicineItem>()

            val result = TherapyUiState(
                currentActiveMedicines = activeTemp,
                currentEndedMedicines = endedTemp,
                done = endedTemp.size,
                amount = endedTemp.size + activeTemp.size,
                progress = endedTemp.size.toFloat() / (activeTemp.size + endedTemp.size)
            )
            currentLoadingState = CurrentLoadingState.Success(result)
        }
        return true
    }

    fun addMedicineItem(medicineItem: MedicineItem) {
        _listUserMedicines.value.add(medicineItem)
    }

    // отображает неделю, на которой находится пользователь (слишком быстро, чтобы выносит в фон)
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

    // служит для отображения индикторов под датами (вызывается из LaunchedEffect, здесь не в фоне)
    fun getAmountActive(date: LocalDate): Int {
        return _listUserMedicines.value.filter {
            it.startDate == date || (date.isAfter(it.startDate) && date.isBefore(
                it.endDate
            ))
        }.size
    }

    fun getCurrentDate(): LocalDate {
        return currentDate.first();
    }
}

// удобно для использования в случае загрузки
sealed interface CurrentLoadingState {
    data class Success(val therapyuiState: TherapyUiState) : CurrentLoadingState
    object Loading : CurrentLoadingState
}