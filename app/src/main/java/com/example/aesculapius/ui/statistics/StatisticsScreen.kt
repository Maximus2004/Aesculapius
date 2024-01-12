package com.example.aesculapius.ui.statistics

import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
import com.patrykandpatrick.vico.compose.axis.vertical.rememberEndAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollSpec
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.DefaultDimens
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.PercentageFormatAxisValueFormatter
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.chart.decoration.ThresholdLine
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.extension.half
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerVisibilityChangeListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.VerticalPosition
import com.patrykandpatrick.vico.core.entry.ChartModelProducer

@Composable
fun StatisticsScreen(modifier: Modifier = Modifier) {
    val statisticsViewModel: StatisticsViewModel = viewModel()
    val staticsUiState = statisticsViewModel.staticsUiState.collectAsState().value

    var isLineChart by remember { mutableStateOf(true) }

    // can't init with by, because fun hasn't get
    val modelProducer = remember { ChartEntryModelProducer() }
    val modelProducerColumn = remember { ChartEntryModelProducer() }

    // dates for bar and line charts, setting styles for line charts
    val datasetLineSpec = remember { arrayListOf<LineChart.LineSpec>() }
    val datasetForModel = remember { mutableStateListOf<List<FloatEntry>>() }
    val datasetForModelColumn = remember { mutableStateListOf<List<FloatEntry>>() }
    val datasetDates = remember { arrayListOf<LocalDate>() }
    val datasetDatesColumn = remember { arrayListOf<LocalDate>() }

    // states for data changes
    var dateText by remember { mutableStateOf(LocalDate.now()) }
    var dateTextColumn by remember { mutableStateOf(LocalDate.now()) }
    var pointsAmountText by remember { mutableIntStateOf(-1) }
    var dateBegin by remember { mutableStateOf(LocalDate.now().minusDays(6)) }

    // CouroutineScope launches every time when key changes (imitation of fetching data)
    LaunchedEffect(key1 = staticsUiState) {
        datasetForModel.clear()
        datasetLineSpec.clear()
        datasetDates.clear()
        datasetForModelColumn.clear()
        datasetDatesColumn.clear()

        // fetch data for column chart (get points and dates for every x)
        var xPosColumn = 0f
        val dataColumnPoints = arrayListOf<FloatEntry>()
        for (i in 1..12) {
            val randomYFloat = (0..25).random().toFloat()
            dataColumnPoints.add(FloatEntry(x = xPosColumn, y = randomYFloat))
            xPosColumn += 1f
            datasetDatesColumn.add(LocalDate.now().minusMonths((12 - i).toLong()))
        }

        // setting styles for line chart
        datasetLineSpec.add(
            LineChart.LineSpec(
                lineColor = Color(0xFF6750A4).toArgb(),
                lineThicknessDp = 4f
            )
        )

        // depending on different time periods, set dateBegin, dataPoints and datasetDates
        var xPos = 0f
        val dataPoints = arrayListOf<FloatEntry>()
        when (staticsUiState.graphicTypes) {
            GraphicTypes.Week -> {
                dateBegin = LocalDate.now().minusDays(6)
                for (i in 1..7) {
                    val randomYFloat = (0..500).random().toFloat()
                    dataPoints.add(FloatEntry(x = xPos, y = randomYFloat))
                    xPos += 1f
                    datasetDates.add(LocalDate.now().minusDays((7 - i).toLong()))
                }
            }

            GraphicTypes.Month -> {
                dateBegin = LocalDate.now().minusMonths(1)
                for (i in 1..ChronoUnit.DAYS.between(dateBegin, LocalDate.now())) {
                    val randomYFloat = (0..500).random().toFloat()
                    dataPoints.add(FloatEntry(x = xPos, y = randomYFloat))
                    xPos += 1f
                    datasetDates.add(LocalDate.now().minusDays((30 - i).toLong()))
                }
            }

            GraphicTypes.ThreeMonths -> {
                dateBegin = LocalDate.now().minusMonths(3)
                var tempSum = 0.0f
                for (i in 1..ChronoUnit.DAYS.between(dateBegin, LocalDate.now())) {
                    val randomYFloat = (0..500).random().toFloat()
                    tempSum += randomYFloat
                    if ((i % 7).toInt() == 0) {
                        dataPoints.add(FloatEntry(x = xPos, y = tempSum / 7))
                        xPos += 1f
                        datasetDates.add(LocalDate.now().minusDays((90 - i)))
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
                        dataPoints.add(FloatEntry(x = xPos, y = tempSum / 7))
                        xPos += 1f
                        datasetDates.add(LocalDate.now().minusDays((180 - i)))
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
                        dataPoints.add(FloatEntry(x = xPos, y = tempSum / 7))
                        xPos += 1f
                        datasetDates.add(LocalDate.now().minusDays((364 - i)))
                        tempSum = 0.0f
                    }
                }
            }
        }

        // add points to model for line chart
        datasetForModel.add(dataPoints)
        modelProducer.setEntries(datasetForModel)

        // add points to model for column chart
        datasetForModelColumn.add(dataColumnPoints)
        modelProducerColumn.setEntries(datasetForModelColumn)
    }
    Box() {
        LazyColumn(modifier = modifier) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = if (isLineChart) "Значения ПСВ" else "АСТ тестирование",
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
                            if (!isLineChart)
                                DisplayDatesForColumn(
                                    pointsAmountText = pointsAmountText,
                                    dateTextColumn = dateTextColumn
                                )
                            else
                                DisplayDatesForLine(
                                    graphicTypes = staticsUiState.graphicTypes,
                                    dateText = dateText, dateBegin = dateBegin
                                )
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
                    val assets = LocalContext.current
                    val customTypeface =
                        Typeface.createFromAsset(assets.assets, "inter_regular.ttf")

                    ProvideChartStyle {
                        if (isLineChart)
                            ShowLineChart(
                                datasetForModel = datasetForModel,
                                datasetDates = datasetDates,
                                modelProducer = modelProducer,
                                datasetLineSpec = datasetLineSpec,
                                onChangeMarker = { dateText = it },
                                typeface = customTypeface
                            )
                        else {
                            ShowColumnChart(
                                typeface = customTypeface,
                                onDataChanged = { x, y ->
                                    pointsAmountText = y
                                    dateTextColumn = datasetDatesColumn[x]
                                },
                                modelProducerColumn = modelProducerColumn
                            )
                        }
                    }
                }
                if (isLineChart) {
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
                                    modifier = Modifier.padding(
                                        horizontal = 8.dp,
                                        vertical = 6.dp
                                    )
                                )
                            }
                        }
                    }
                }
                Text(
                    text = "Отслеживание тенденций и изменений",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 32.dp, start = 23.dp, end = 23.dp)
                )
                Text(
                    text = "Следите за динамикой Вашего состояния, используя две диаграммы: столбчатую, отражающую значения АСТ теста, и линейную, представляющую значения ПСВ.",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 8.dp, start = 23.dp, end = 23.dp)
                )
                Spacer(Modifier.height(90.dp))
            }
        }
        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .padding(bottom = 24.dp)
                .height(56.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Скачать статистику",
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ShowColumnChart(
    typeface: Typeface,
    modelProducerColumn: ChartEntryModelProducer,
    onDataChanged: (Int, Int) -> Unit
) {
    val defaultColumns = currentChartStyle.columnChart.columns
    val thresholdLineNullLevel = rememberNullLevel(typeface)
    Chart(
        modifier = Modifier.padding(bottom = 30.dp),
        chart = columnChart(
            columns = remember(defaultColumns) {
                defaultColumns.map { _ ->
                    LineComponent(
                        color = Color(0xFF6750A4).toArgb(),
                        thicknessDp = 8f,
                        shape = Shapes.roundedCornerShape(
                            topLeftPercent = 40,
                            topRightPercent = 40,
                            bottomLeftPercent = 0,
                            bottomRightPercent = 0
                        )
                    )
                }
            },
            decorations = remember(thresholdLineNullLevel) {
                listOf(thresholdLineNullLevel)
            },
            spacing = 14.dp
        ),
        chartModelProducer = modelProducerColumn,
        isZoomEnabled = false,
        chartScrollSpec = rememberChartScrollSpec(isScrollEnabled = false),
        startAxis = rememberStartAxis(
            label = textComponent(
                color = MaterialTheme.colorScheme.secondary,
                textSize = 12.sp,
                typeface = typeface
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
            ),
        ),
        horizontalLayout = HorizontalLayout.FullWidth(
            unscalableStartPaddingDp = 45f,
            unscalableEndPaddingDp = 20f
        ),
        bottomAxis = rememberBottomAxis(
            label = null,
            axis = LineComponent(
                color = MaterialTheme.colorScheme.onSurface.toArgb(),
                thicknessDp = 1f
            ),
            tickLength = 0.dp,
            guideline = null
        ),
        marker = rememberColumnMarker(),
        markerVisibilityChangeListener = object : MarkerVisibilityChangeListener {
            // update data when on marker moved
            override fun onMarkerMoved(
                marker: Marker,
                markerEntryModels: List<Marker.EntryModel>,
            ) {
                super.onMarkerMoved(marker, markerEntryModels)
                onDataChanged(
                    markerEntryModels.first().entry.x.toInt(),
                    markerEntryModels.first().entry.y.toInt()
                )
            }

            // update data on marker shown
            override fun onMarkerShown(
                marker: Marker,
                markerEntryModels: List<Marker.EntryModel>
            ) {
                super.onMarkerShown(marker, markerEntryModels)
                onDataChanged(
                    markerEntryModels.first().entry.x.toInt(),
                    markerEntryModels.first().entry.y.toInt()
                )
            }
        }
    )
}

@Composable
fun DisplayDatesForColumn(pointsAmountText: Int, dateTextColumn: LocalDate) {
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
    Text(
        text =
        (if (pointsAmountText != -1) "${convertToRussian(pointsAmountText)}, "
        else "") + dateTextColumn.format(formatter),
        style = MaterialTheme.typography.bodyLarge
    )
    Text(
        text = "${LocalDate.now().minusYears(1).format(formatter)} - ${
            LocalDate.now().format(formatter)
        }",
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun DisplayDatesForLine(graphicTypes: GraphicTypes, dateText: LocalDate, dateBegin: LocalDate) {
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
    val formatterSide = DateTimeFormatter.ofPattern("d MMMM", Locale("ru"))
    val formatterThreeMonths = DateTimeFormatter.ofPattern("d MMM", Locale("ru"))
    val formatterYear = DateTimeFormatter.ofPattern("d MMM yyyy", Locale("ru"))
    val formatterYearFull = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
    when (graphicTypes) {
        GraphicTypes.Week -> Text(
            text = dateText.format(formatter),
            style = MaterialTheme.typography.bodyLarge
        )

        GraphicTypes.Month -> Text(
            text = dateText.format(formatter),
            style = MaterialTheme.typography.bodyLarge
        )

        GraphicTypes.ThreeMonths -> Text(
            text = "${
                dateText.format(
                    formatterThreeMonths
                )
            } - ${
                dateText.plusDays(6).format(formatterThreeMonths)
            } ${dateText.year}",
            style = MaterialTheme.typography.bodyLarge
        )

        GraphicTypes.HalfYear -> Text(
            text = "${dateText.format(formatterThreeMonths)} - ${
                dateText.plusDays(
                    6
                ).format(formatterThreeMonths)
            } ${dateText.year}", style = MaterialTheme.typography.bodyLarge
        )

        else -> Text(
            text = "${dateText.format(formatterYear)} - ${
                dateText.plusDays(
                    6
                ).format(formatterYear)
            }", style = MaterialTheme.typography.bodyLarge
        )
    }
    when (graphicTypes) {
        GraphicTypes.Year -> Text(
            text = "${dateBegin.format(formatterYearFull)} - ${
                LocalDate.now().format(formatterYearFull)
            }", style = MaterialTheme.typography.bodySmall
        )

        else -> Text(
            text = "${dateBegin.format(formatterSide)} - ${
                LocalDate.now().format(formatterSide)
            }, ${LocalDate.now().year}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private fun convertToRussian(points: Int): String {
    return when {
        (points % 10 == 1 && points != 11) -> "$points балл"
        ((points % 10 == 2 || points % 10 == 3 || points % 10 == 4) && points != 12 && points != 13 && points != 14) -> "$points балла"
        else -> "$points баллов"
    }
}

@Composable
private fun rememberNullLevel(customTypeface: Typeface): ThresholdLine {
    val line = shapeComponent(color = Color.Transparent, strokeColor = Color.Transparent)
    val label = textComponent(
        color = MaterialTheme.colorScheme.secondary,
        textSize = 12.sp,
        typeface = customTypeface
    )
    return remember(line, label) {
        ThresholdLine(
            thresholdValue = 0f,
            lineComponent = line,
            labelComponent = label,
            labelHorizontalPosition = ThresholdLine.LabelHorizontalPosition.Start,
            labelVerticalPosition = ThresholdLine.LabelVerticalPosition.Bottom
        )
    }
}

@Composable
fun ShowLineChart(
    datasetForModel: SnapshotStateList<List<FloatEntry>>,
    datasetLineSpec: ArrayList<LineChart.LineSpec>,
    modelProducer: ChartEntryModelProducer,
    datasetDates: ArrayList<LocalDate>,
    onChangeMarker: (LocalDate) -> Unit,
    typeface: Typeface
) {
    if (datasetForModel.isNotEmpty()) {
        val thresholdLine = rememberNullLevel(typeface)
        Chart(
            chart = lineChart(
                lines = datasetLineSpec,
                decorations = remember(thresholdLine) { listOf(thresholdLine) }
            ),
            chartModelProducer = modelProducer,
            isZoomEnabled = false,
            chartScrollSpec = rememberChartScrollSpec(isScrollEnabled = false),
            startAxis = rememberStartAxis(
                label = textComponent(
                    color = MaterialTheme.colorScheme.secondary,
                    textSize = 12.sp,
                    typeface = typeface
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
                label = null,
                axis = LineComponent(
                    color = MaterialTheme.colorScheme.onSurface.toArgb(),
                    thicknessDp = 1f
                ),
                tickLength = 0.dp,
                guideline = null
            ),
            marker = rememberMarker(),
            markerVisibilityChangeListener = object : MarkerVisibilityChangeListener {
                // update data when on marker moved
                override fun onMarkerMoved(
                    marker: Marker,
                    markerEntryModels: List<Marker.EntryModel>,
                ) {
                    super.onMarkerMoved(marker, markerEntryModels)
                    onChangeMarker(datasetDates[markerEntryModels.first().entry.x.toInt()])
                }

                // update data on marker shown
                override fun onMarkerShown(
                    marker: Marker,
                    markerEntryModels: List<Marker.EntryModel>
                ) {
                    super.onMarkerShown(marker, markerEntryModels)
                    onChangeMarker(datasetDates[markerEntryModels.first().entry.x.toInt()])
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StatisticsScreenPreview() {
    AesculapiusTheme {
        StatisticsScreen()
    }
}