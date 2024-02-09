package com.example.aesculapius.ui.tests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aesculapius.database.AesculapiusRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TestsViewModel @Inject constructor(private val aesculapiusRepository: AesculapiusRepository): ViewModel() {
    private var _summaryScore = MutableStateFlow(0)
    val summaryScore: StateFlow<Int> = _summaryScore

    /** [updateSummaryScore] - вызывается при завершении теста и отправляет результаты в Room */
    fun updateSummaryScore(score: Int) = viewModelScope.launch {
        _summaryScore.value = score
        aesculapiusRepository.insertAstTestScore(LocalDate.now(), score)
    }

    fun insertNewMetrics(first: Float, second: Float, third: Float) = viewModelScope.launch {
        aesculapiusRepository.insertMetrics(first + second + third / 3, LocalDate.now())
    }

    fun updateNewMetrics(first: Float, second: Float, third: Float) = viewModelScope.launch {
        if (aesculapiusRepository.getAllMetricsWithDate(LocalDate.now()).isEmpty())
            aesculapiusRepository.insertMetrics((first + second + third) / 3, LocalDate.now())
        else
            aesculapiusRepository.updateMetrics((first + second + third) / 3, LocalDate.now())
    }
}