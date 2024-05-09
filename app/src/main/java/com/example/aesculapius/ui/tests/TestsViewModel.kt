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

    suspend fun getTestsScore(): Double = viewModelScope.async {
        val successMetrics = aesculapiusRepository.getLinePointsAmountOnDates(
            LocalDate.now().minusMonths(1), LocalDate.now()
        ) * 2.0 / 60.0
        val astResults = aesculapiusRepository.getAllAstResults()
        val successScores =
            if (astResults.isEmpty()) 0.0
            else
                if (astResults.last().date.isAfter(
                        LocalDate.now().minusMonths(1)
                    ) || astResults.last().date == LocalDate.now().minusMonths(1)
                ) astResults.last().score / 25.0 else 0.0
        successScores + successMetrics
    }.await()
}