package com.example.aesculapius.ui.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aesculapius.database.AesculapiusRepository
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class StatisticsViewModel @Inject constructor(private val aesculapiusRepository: AesculapiusRepository) :
    ViewModel() {
    private val _statisticsUiState = MutableStateFlow(GraphicTypeContent())
    val statisticsUiState: StateFlow<GraphicTypeContent> = _statisticsUiState

    private val _chartEntryModelColumn = MutableStateFlow(ChartEntryModelProducer())
    val chartEntryModelColumn = _chartEntryModelColumn

    private val _chartEntryModelLine = MutableStateFlow(ChartEntryModelProducer())
    val chartEntryModelLine = _chartEntryModelLine

    private val _listLocalDate = MutableStateFlow(mutableListOf<LocalDate>())
    val datesForLineChart = _listLocalDate

    // отслеживаем, когда приходят новые изменения и разбиваем данные на точки и даты для каждой точки
    val datesForColumnChart = aesculapiusRepository.getAllAstResultsInRange().map {
        val tempEntries: MutableList<FloatEntry> = mutableListOf()
        val tempDates: MutableList<LocalDate> = mutableListOf()
        it.forEachIndexed { index, item ->
            tempEntries.add(FloatEntry(index.toFloat(), item.score.toFloat()))
            tempDates.add(item.date)
        }
        _chartEntryModelColumn.value.setEntries(tempEntries)
        tempDates
    }
        .stateIn(
            scope = viewModelScope,
            SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = listOf()
        )

    suspend fun getLinePointsAmountOnDates(startDate: LocalDate, endDate: LocalDate): Int = viewModelScope.async {
        aesculapiusRepository.getLinePointsAmountOnDates(startDate, endDate)
    }.await()

    suspend fun getColumnPointsAmountOnDates(startDate: LocalDate, endDate: LocalDate): Int = viewModelScope.async {
        aesculapiusRepository.getColumnPointsAmountOnDates(startDate, endDate)
    }.await()

    fun setMetricsOnDatesShort(startDate: LocalDate, endDate: LocalDate) = viewModelScope.launch {
        val tempEntries: MutableList<FloatEntry> = mutableListOf()
        _listLocalDate.value = mutableListOf()
        aesculapiusRepository.getAllMetricsInRange(startDate, endDate).forEachIndexed { index, item ->
            tempEntries.add(FloatEntry(index.toFloat(), item.metrics))
            _listLocalDate.value.add(item.date)
        }
        _chartEntryModelLine.value.setEntries(tempEntries)
    }

    fun initLineChartData() = viewModelScope.launch {
        aesculapiusRepository.deleteAllMetrics()
        repeat(20) { index ->
            aesculapiusRepository.insertMetrics(Random.nextFloat()*1000, LocalDate.now().minusDays((20 - index).toLong()))
        }
    }

    fun setMetricsOnDatesLong(startDate: LocalDate, endDate: LocalDate) = viewModelScope.launch {
        val tempEntries: MutableList<FloatEntry> = mutableListOf()
        _listLocalDate.value = mutableListOf()
        var tempCount = 0f
        aesculapiusRepository.getAllMetricsInRange(startDate, endDate)
            .forEachIndexed { index, item ->
                tempCount += item.metrics
                if (index % 7 == 6) {
                    tempEntries.add(
                        FloatEntry(
                            ((index + 1) / 7 - 1).toFloat(),
                            String.format("%.1f", tempCount / 7).replace(",", ".").toFloat()
                        )
                    )
                    _listLocalDate.value.add(item.date.minusDays(6))
                    tempCount = 0f
                }
            }
        _chartEntryModelLine.value.setEntries(tempEntries)
    }

    fun updateCurrentNavType(typeContent: GraphicTypeContent) {
        _statisticsUiState.update { typeContent }
    }

    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }
}