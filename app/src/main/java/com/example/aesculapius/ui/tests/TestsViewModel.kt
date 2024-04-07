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

    fun onTestsEvent(event: TestsEvent) = viewModelScope.launch {
        when (event) {
            is TestsEvent.OnInsertNewMetrics -> {
                aesculapiusRepository.insertMetrics((event.first + event.second + event.third) / 3, LocalDate.now())
            }
            is TestsEvent.OnUpdateNewMetrics -> {
                if (aesculapiusRepository.getAllMetricsWithDate(LocalDate.now()).isEmpty())
                    aesculapiusRepository.insertMetrics((event.first + event.second + event.third) / 3, LocalDate.now())
                else
                    aesculapiusRepository.updateMetrics((event.first + event.second + event.third) / 3, LocalDate.now())
            }
            is TestsEvent.OnUpdateSummaryScore -> {
                _summaryScore.value = event.score
                aesculapiusRepository.insertAstTestScore(LocalDate.now(), event.score)
            }
        }
    }
}