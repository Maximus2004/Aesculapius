package com.example.aesculapius.ui.therapy

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.aesculapius.data.Medicine
import com.example.aesculapius.data.medicines
import com.example.aesculapius.ui.home.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class TherapyViewModel : ViewModel() {
    // при изменении выбранного дня, меняется currentDate вместе с state, который отвечает за отображение
    // дня в календаре. при изменении currentDate внутри списка не происходит recomposition
    // таким образом данная "уловка" позволяет не отрисовывать календарь дважды, а только получать
    // текущую дату, когда это необходимо для отображения лекарств
    val currentDate = mutableStateListOf(LocalDate.now())

    // актуаьные лекарства на этот день
    private var _currentMedicines = MutableStateFlow(TherapyUiState())
    val currentMedicines: StateFlow<TherapyUiState> = _currentMedicines

    // прогресс для текущего дня
    private var _currentProgress = MutableStateFlow(ProgressUiState())
    val currentProgress: StateFlow<ProgressUiState> = _currentProgress

    // при обновлении даты требудет обновить список лекарств к употреблению в это время
    fun updateCurrentDate(newDate: LocalDate) {
        currentDate[0] = newDate
        val activeTemp =
            medicines[currentDate.first().month.value - 1][currentDate.first().dayOfMonth - 1].active
        val endedTemp =
            medicines[currentDate.first().month.value - 1][currentDate.first().dayOfMonth - 1].ended
        _currentMedicines.update {
            TherapyUiState(
                currentActiveMedicines = activeTemp,
                currentEndedMedicines = endedTemp
            )
        }
        _currentProgress.update {
            ProgressUiState(
                done = endedTemp.size,
                amount = endedTemp.size + activeTemp.size,
                progress = endedTemp.size.toFloat() / (activeTemp.size + endedTemp.size)
            )
        }
    }

    fun getCurrentDate(): LocalDate {
        return currentDate.first();
    }
}