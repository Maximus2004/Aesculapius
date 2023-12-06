package com.example.aesculapius.ui.statistics

import android.graphics.Typeface
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aesculapius.R
import com.example.aesculapius.data.graphicsNavigationItemContentList
import com.example.aesculapius.ui.theme.AesculapiusTheme
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerVisibilityChangeListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale


@Composable
fun StatisticsScreen() {
    val statisticsViewModel: StatisticsViewModel = viewModel()
    val staticsUiState = statisticsViewModel.staticsUiState.collectAsState().value

    var isLineChart by remember { mutableStateOf(true) }

    // не может быть инициализировано через by, так как fun hasn't get
    val modelProducer = remember { ChartEntryModelProducer() }

    val datasetForModel = remember { mutableStateListOf<List<FloatEntry>>() }
    val datasetLineSpec = remember { arrayListOf<LineChart.LineSpec>() }
    val datasetDates = remember { arrayListOf<LocalDate>() }

    var dateText by remember { mutableStateOf(LocalDate.now()) }
    var dateBegin by remember { mutableStateOf(LocalDate.now().minusDays(6)) }
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
    val formatterSide = DateTimeFormatter.ofPattern("d MMMM", Locale("ru"))
    val formatterThreeMonths = DateTimeFormatter.ofPattern("d MMM", Locale("ru"))
    val formatterYear = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("ru"))
    val formatterYearFull = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))

    // CouroutineScope, который будет запускаться всякий раз при измении ключа
    LaunchedEffect(key1 = staticsUiState) {
        datasetForModel.clear()
        datasetLineSpec.clear()
        datasetDates.clear()

        var xPos = 0f
        val dataPoints = arrayListOf<FloatEntry>()
        datasetLineSpec.add(
            LineChart.LineSpec(
                lineColor = Color(0xFF6750A4).toArgb(),
                lineThicknessDp = 4f
            )
        )
        when(staticsUiState.graphicTypes) {
            GraphicTypes.Week -> {
                dateBegin = LocalDate.now().minusDays(6)
                for (i in 1..7) {
                    val randomYFloat = (0..500).random().toFloat()
                    dataPoints.add(FloatEntry(x = xPos, y = randomYFloat))
                    xPos += 1f
                    datasetDates.add(LocalDate.now().minusDays((7-i).toLong()))
                }
            }
            GraphicTypes.Month -> {
                dateBegin = LocalDate.now().minusMonths(1)
                for (i in 1..ChronoUnit.DAYS.between(dateBegin, LocalDate.now())) {
                    val randomYFloat = (0..500).random().toFloat()
                    dataPoints.add(FloatEntry(x = xPos, y = randomYFloat))
                    xPos += 1f
                    datasetDates.add(LocalDate.now().minusDays((30-i).toLong()))
                }
            }
           GraphicTypes.ThreeMonths -> {
                dateBegin = LocalDate.now().minusMonths(3)
                var tempSum = 0.0f
                for (i in 1..ChronoUnit.DAYS.between(dateBegin, LocalDate.now())) {
                    val randomYFloat = (0..500).random().toFloat()
                    tempSum += randomYFloat
                    if ((i % 7).toInt() == 0) {
                        dataPoints.add(FloatEntry(x = xPos, y = tempSum/7))
                        xPos += 1f
                        datasetDates.add(LocalDate.now().minusDays((90-i)))
                        tempSum = 0.0f
                    }
                }
            }
            GraphicTypes.HalfYear -> {
                dateBegin = LocalDate.now().minusMonths(6)
                var tempSum = 0.0f
                for (i in 1..ChronoUnit.DAYS.between(dateBegin, LocalDate.now())) {
                    val randomYFloat = (0..500).random().toFloat()
                    tempSum += randomYFloat
                    if ((i % 7).toInt() == 0) {
                        dataPoints.add(FloatEntry(x = xPos, y = tempSum/7))
                        xPos += 1f
                        datasetDates.add(LocalDate.now().minusDays((180-i)))
                        tempSum = 0.0f
                    }
                }
            }
            else -> {
                dateBegin = LocalDate.now().minusYears(1)
                var tempSum = 0.0f
                for (i in 1..ChronoUnit.DAYS.between(dateBegin, LocalDate.now())) {
                    val randomYFloat = (0..500).random().toFloat()
                    tempSum += randomYFloat
                    if ((i % 7).toInt() == 0) {
                        dataPoints.add(FloatEntry(x = xPos, y = tempSum/7))
                        xPos += 1f
                        datasetDates.add(LocalDate.now().minusDays((364-i)))
                        tempSum = 0.0f
                    }
                }
            }
        }
        datasetForModel.add(dataPoints)
        modelProducer.setEntries(datasetForModel)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = Color.White)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Значения ПСВ",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(vertical = 22.dp)
                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column() {
                    when(staticsUiState.graphicTypes){
                        GraphicTypes.Week -> Text(text = dateText.format(formatter), style = MaterialTheme.typography.bodyLarge)
                        GraphicTypes.Month -> Text(text = dateText.format(formatter), style = MaterialTheme.typography.bodyLarge)
                        GraphicTypes.ThreeMonths -> Text(text = "${dateText.format(formatterThreeMonths)} - ${dateText.plusDays(6).format(formatterThreeMonths)} ${dateText.year}", style = MaterialTheme.typography.bodyLarge)
                        GraphicTypes.HalfYear -> Text(text = "${dateText.format(formatterThreeMonths)} - ${dateText.plusDays(6).format(formatterThreeMonths)} ${dateText.year}", style = MaterialTheme.typography.bodyLarge)
                        else -> Text(text = "${dateText.format(formatterYear)} - ${dateText.plusDays(6).format(formatterYear)}", style = MaterialTheme.typography.bodyLarge)
                    }
                    when(staticsUiState.graphicTypes) {
                        GraphicTypes.Year -> Text(text = "${ dateBegin.format(formatterYearFull) } - ${ LocalDate.now().format(formatterYearFull) }", style = MaterialTheme.typography.bodySmall)
                        else -> Text(text = "${ dateBegin.format(formatterSide) } - ${ LocalDate.now().format(formatterSide) }, ${LocalDate.now().year}", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Spacer(Modifier.weight(1f))
                Card(
                    elevation = 0.dp,
                    modifier = Modifier
                        .clickable { isLineChart = !isLineChart }
                        .wrapContentSize(),
                    shape = RoundedCornerShape(16.dp),
                    backgroundColor = MaterialTheme.colorScheme.secondary
                ) {
                    Row(modifier = Modifier.padding(4.dp)) {
                        Card(
                            modifier = Modifier.size(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            elevation = 0.dp,
                            backgroundColor = if (isLineChart) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.line_graphic_icon),
                                contentDescription = null,
                                alignment = Alignment.Center,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(10.dp)
                            )
                        }
                        Card(
                            modifier = Modifier.size(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            elevation = 0.dp,
                            backgroundColor = if (!isLineChart) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.bar_chart_icon),
                                contentDescription = null,
                                alignment = Alignment.Center,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(10.dp)
                            )
                        }
                    }
                }
            }
            if (datasetForModel.isNotEmpty()) {
                ProvideChartStyle {
                    val marker = rememberMarker()
                    val assets = LocalContext.current
                    val customTypeface = Typeface.createFromAsset(assets.assets, "inter_regular.ttf")
                    Chart(
                        chart = lineChart(lines = datasetLineSpec),
                        chartModelProducer = modelProducer,
                        isZoomEnabled = false,
                        chartScrollSpec = rememberChartScrollSpec(isScrollEnabled = false),
                        startAxis = rememberStartAxis(
                            title = "Top values",
                            label = textComponent(
                                color = MaterialTheme.colorScheme.secondary,
                                textSize = 12.sp,
                                typeface = customTypeface
                            ),
                            valueFormatter = { value, _ -> value.toInt().toString() },
                            tickLength = 0.dp,
                            tick = LineComponent(color = Color.Transparent.toArgb()),
                            axis = LineComponent(color = Color.Transparent.toArgb()),
                            verticalLabelPosition = VerticalAxis.VerticalLabelPosition.Top,
                            horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside,
                            itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = 6),
                            guideline = LineComponent(
                                strokeColor = MaterialTheme.colorScheme.onSurface.toArgb(),
                                color = MaterialTheme.colorScheme.onSurface.toArgb(),
                                thicknessDp = 1f
                            )
                        ),
                        horizontalLayout = HorizontalLayout.FullWidth(
                            unscalableStartPaddingDp = 45f,
                            unscalableEndPaddingDp = 20f
                        ),
                        bottomAxis = rememberBottomAxis(
                            title = "Count of values",
                            label = null,
                            axis = LineComponent(
                                color = MaterialTheme.colorScheme.onSurface.toArgb(),
                                thicknessDp = 1f
                            ),
                            tickLength = 0.dp,
                            guideline = null
                        ),
                        marker = marker,
                        markerVisibilityChangeListener = object : MarkerVisibilityChangeListener {
                            override fun onMarkerMoved(
                                marker: Marker,
                                markerEntryModels: List<Marker.EntryModel>,
                            ) {
                                dateText = datasetDates[markerEntryModels.first().entry.x.toInt()]
                            }
                        }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 32.dp, bottom = 16.dp, start = 3.dp)
                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                graphicsNavigationItemContentList.forEach {
                    Card(
                        modifier = Modifier
                            .height(32.dp)
                            .wrapContentWidth()
                            .clickable { statisticsViewModel.updateCurrentNavType(it) }
                            .padding(end = 3.dp),
                        backgroundColor = if (it == staticsUiState) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
                        contentColor = if (it == staticsUiState) Color.White else MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp),
                        elevation = 0.dp
                    ) {
                        Text(
                            text = it.nameOfType,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticsScreenPreview() {
    AesculapiusTheme {
        StatisticsScreen()
    }
}