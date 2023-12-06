package com.example.aesculapius.ui.therapy

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aesculapius.data.Medicine
import com.example.aesculapius.data.medicines
import com.example.aesculapius.ui.home.HomeUiState
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import io.github.boguszpawlowski.composecalendar.week.Week
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    // актуальные данные для выбранного дня
    private var _currentMedicines = MutableStateFlow(TherapyUiState())
    val currentMedicines: StateFlow<TherapyUiState> = _currentMedicines

    private var _currentWeekDates = MutableStateFlow(Week.now())
    val currentWeekDates: StateFlow<Week> = _currentWeekDates

    // при обновлении даты требудет обновить список лекарств к употреблению в это время
    fun updateCurrentDate(newDate: LocalDate): Boolean {
        Log.i("TAGTAG", newDate.toString())
        viewModelScope.launch {
            currentDate[0] = newDate
            val activeTemp =
                medicines[currentDate.first().month.value - 1][currentDate.first().dayOfMonth - 1].active
            val endedTemp =
                medicines[currentDate.first().month.value - 1][currentDate.first().dayOfMonth - 1].ended
            _currentMedicines.update {
                TherapyUiState(
                    currentActiveMedicines = activeTemp,
                    currentEndedMedicines = endedTemp,
                    done = endedTemp.size,
                    amount = endedTemp.size + activeTemp.size,
                    progress = endedTemp.size.toFloat() / (activeTemp.size + endedTemp.size)
                )
            }
        }
        return true
    }

    fun getWeekDates(currentDate: LocalDate) {
        viewModelScope.launch(Dispatchers.Default) {
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
    }

    fun getAmountActive(state: DayState<DynamicSelectionState>): Int {
        return medicines[state.date.month.value - 1][state.date.dayOfMonth - 1].active.size
    }

    fun getCurrentDate(): LocalDate {
        return currentDate.first();
    }
}