package com.example.aesculapius.ui.tests

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aesculapius.database.AesculapiusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TestsViewModel @Inject constructor(private val aesculapiusRepository: AesculapiusRepository) : ViewModel() {
    private var _summaryScore = MutableStateFlow(0)
    val summaryScore: StateFlow<Int> = _summaryScore

    fun onTestsEvent(event: TestsEvent) = viewModelScope.launch {
        when (event) {
            is TestsEvent.OnInsertNewMetrics -> {
                aesculapiusRepository.insertMetrics(
                    (event.first + event.second + event.third) / 3,
                    LocalDate.now()
                )

            }

            is TestsEvent.OnUpdateNewMetrics -> {
                if (aesculapiusRepository.getAllMetricsWithDate(LocalDate.now()).isEmpty())
                    aesculapiusRepository.insertMetrics(
                        (event.first + event.second + event.third) / 3,
                        LocalDate.now()
                    )
                else
                    aesculapiusRepository.updateMetrics(
                        (event.first + event.second + event.third) / 3,
                        LocalDate.now()
                    )
            }

            is TestsEvent.OnUpdateSummaryScore -> {
                _summaryScore.value = event.score
                if (event.isAstTest)
                    aesculapiusRepository.insertAstTestScore(LocalDate.now(), event.score)
            }
        }
    }

    // некоторые функции, вызываемые в методе, требуют асинхронного выполнения, поэтому сама функция тоже асинхронна
    suspend fun getTestsScore(): Pair<Double, Double> = viewModelScope.async {
        // подсчитывается количество фактов ввода показателей пикфлоуметрии и делится на 60
        val successMetrics = aesculapiusRepository.getLinePointsAmountOnDates(
            LocalDate.now().minusMonths(1), LocalDate.now()
        ) * 2.0 / 60.0

        // берём все показателя АСТ-тестов и записываем их в список
        val astResults = aesculapiusRepository.getAllAstResults()
        val successScores =
            // если за последнее время не было пройдено ни одного АСТ-теста
            if (astResults.isEmpty()) 0.0
            else
                // если тест был пройден и последний показатель был добавлен не раньше месяца назад
                if (astResults.last().date.isAfter(LocalDate.now().minusMonths(1)) || astResults.last().date == LocalDate.now().minusMonths(1))
                    // берём последний показатель и делим на 25.0
                    astResults.last().score / 25.0
                else 0.0
        // возвращаем пару значений: коэф отвечающий за введёённые метрики и коэф отвечающий за последний показатель АСТ-теста
        Pair(successScores, successMetrics)
    }.await()
}