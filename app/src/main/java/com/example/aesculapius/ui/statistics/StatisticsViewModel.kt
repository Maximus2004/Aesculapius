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

    /** [chartEntryModelColumn] здесь составляется актуальный список точек, отображаемых на столбчатом графке */
    private val _chartEntryModelColumn = MutableStateFlow(ChartEntryModelProducer())
    val chartEntryModelColumn = _chartEntryModelColumn

    /** [chartEntryModelLine] здесь составляется актуальный список точек, отображаемых на линейном графике */
    private val _chartEntryModelLine = MutableStateFlow(ChartEntryModelProducer())
    val chartEntryModelLine = _chartEntryModelLine

    /** [datesForLineChart] даты задаются каждый раз при смене выбранного периода */
    private val _listLocalDate = MutableStateFlow(mutableListOf<LocalDate>())
    val datesForLineChart = _listLocalDate

    /** [datesForColumnChart] отслеживаем, когда приходят новые изменения и разбиваем данные
     * на точки и даты для каждой точки. Мы делаем так со столбчатым графиком, так как он
     * пользователь не может ранжировать его по периоду */
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
            SharingStarted.WhileSubscribed(5000L),
            initialValue = listOf()
        )

    /** [getLinePointsAmountOnDates] прежде чем отображать линейные графики, мы получаем информацию
     * о количестве точек если точек недостаточно для отображения графиков, выводится экран "Нет данных..." */
    suspend fun getLinePointsAmountOnDates(startDate: LocalDate, endDate: LocalDate): Int = viewModelScope.async {
        aesculapiusRepository.getLinePointsAmountOnDates(startDate, endDate)
    }.await()

    /** [getColumnPointsAmountOnDates] прежде чем отображать столбчатые графики, мы получаем информацию
     *  о количестве точек если точек недостаточно для отображения графиков, выводится экран "Нет данных..." */
    suspend fun getColumnPointsAmountOnDates(startDate: LocalDate, endDate: LocalDate): Int = viewModelScope.async {
        aesculapiusRepository.getColumnPointsAmountOnDates(startDate, endDate)
    }.await()

    fun convertToRussian(points: Int): String {
        return when {
            (points % 10 == 1 && points != 11) -> "$points балл"
            ((points % 10 == 2 || points % 10 == 3 || points % 10 == 4) && points != 12 && points != 13 && points != 14) -> "$points балла"
            else -> "$points баллов"
        }
    }

    /** [setMetricsOnDatesShort] задаём точки (Entries) и список дат для короткого периода (неделя/месяц) */
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

    /** [setMetricsOnDatesLong] задаём точки (Entries) и список дат для длинного периода */
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
}