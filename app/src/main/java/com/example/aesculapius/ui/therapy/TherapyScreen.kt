package com.example.aesculapius.ui.therapy

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.aesculapius.R
import com.example.aesculapius.data.CurrentMedicineType
import com.example.aesculapius.ui.navigation.NavigationDestination
import com.example.aesculapius.ui.theme.AesculapiusTheme
import com.example.aesculapius.ui.theme.onErrorContainer
import com.example.aesculapius.ui.theme.onPrimaryContainer
import com.example.aesculapius.ui.theme.secondary
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.SelectableWeekCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.header.WeekState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.rememberSelectableWeekCalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import io.github.boguszpawlowski.composecalendar.week.Week
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

object TherapyScreen : NavigationDestination {
    override val route = "TherapyScreen"
}

@Composable
fun TherapyScreen(
    getAmountNotAcceptedMedicines: suspend (LocalDate) -> Int,
    therapyEvent: (TherapyEvent) -> Unit,
    currentDate: LocalDate,
    updateCurrentDate: (LocalDate) -> Boolean,
    currentLoadingState: CurrentLoadingState,
    generalLoadingState: GeneralLoadingState,
    currentWeekDates: Week,
    onNavigate: (String) -> Unit,
    isAfterCurrentDate: Boolean,
    isWeek: Boolean,
    onClickMedicine: (MedicineCard) -> Unit,
    modifier: Modifier = Modifier
) {
    var isMorningMedicines by remember { mutableStateOf(true) }

    Box {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            when (generalLoadingState) {
                is GeneralLoadingState.Loading -> {
                    item {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(top = 200.dp)
                            )
                        }
                    }
                }

                is GeneralLoadingState.Success -> {
                    item {
                        CalendarItem(
                            getAmountNotAcceptedMedicines = getAmountNotAcceptedMedicines,
                            currentDate = currentDate,
                            onDateChanged = { updateCurrentDate(it) },
                            weekDates = currentWeekDates,
                            isWeek = isWeek,
                            therapyEvent = therapyEvent
                        )
                    }
                    when (currentLoadingState) {
                        is CurrentLoadingState.Loading -> item {
                            Box(modifier = Modifier.fillMaxSize()) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(top = 200.dp)
                                )
                            }
                        }

                        is CurrentLoadingState.Success -> {

                            val currentMedicines = currentLoadingState.therapyUiState

                            item {
                                Card(
                                    elevation = 0.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .padding(vertical = 29.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(
                                            horizontal = 24.dp, vertical = 16.dp
                                        )
                                    ) {
                                        Text(
                                            text = stringResource(R.string.everyday_progress),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        LinearProgressIndicator(
                                            progress = currentMedicines.progress,
                                            color = MaterialTheme.colorScheme.primary,
                                            trackColor = MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier
                                                .padding(top = 12.dp, bottom = 10.dp)
                                                .height(6.dp)
                                                .fillMaxWidth()
                                                .clip(MaterialTheme.shapes.small)
                                        )
                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                text = stringResource(
                                                    R.string.done_medicines,
                                                    currentMedicines.done,
                                                    currentMedicines.amount
                                                ),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onError
                                            )
                                            Spacer(Modifier.weight(1f))
                                            Text(
                                                text = "${(currentMedicines.progress * 100).toInt()}%",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onError
                                            )
                                        }
                                    }
                                }

                                if (isAfterCurrentDate) {
                                    Row(modifier = Modifier.padding(bottom = 24.dp)) {
                                        Button(
                                            onClick = {},
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.secondary,
                                                contentColor = MaterialTheme.colorScheme.tertiaryContainer
                                            ),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = stringResource(R.string.future_medicines),
                                                style = MaterialTheme.typography.headlineSmall
                                            )
                                        }
                                        Spacer(Modifier.width(8.dp))
                                        Spacer(Modifier.weight(1f))
                                    }
                                } else {
                                    Row(modifier = Modifier.padding(bottom = 24.dp)) {
                                        Box(modifier = Modifier.weight(1f)) {
                                            Button(
                                                onClick = { isMorningMedicines = true },
                                                shape = RoundedCornerShape(8.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (isMorningMedicines) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                                                    contentColor = if (isMorningMedicines) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.primary
                                                ),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = stringResource(id = R.string.morning_text),
                                                    style = MaterialTheme.typography.headlineSmall
                                                )
                                            }
                                            if (currentDate == LocalDate.now() && currentMedicines.currentMorningMedicines.any { !it.isAccepted && !it.isSkipped })
                                                Canvas(
                                                    modifier = Modifier
                                                        .padding(start = 9.dp)
                                                        .size(12.dp)
                                                        .align(Alignment.TopEnd)
                                                ) {
                                                    drawCircle(
                                                        color = onErrorContainer,
                                                        center = center
                                                    )
                                                }
                                        }
                                        Spacer(Modifier.width(8.dp))
                                        Box(modifier = Modifier.weight(1f)) {
                                            Button(
                                                onClick = { isMorningMedicines = false },
                                                shape = RoundedCornerShape(8.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (!isMorningMedicines) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                                                    contentColor = if (!isMorningMedicines) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.primary
                                                ),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = stringResource(id = R.string.evening_text),
                                                    style = MaterialTheme.typography.headlineSmall
                                                )
                                            }
                                            if (currentDate == LocalDate.now() && currentMedicines.currentEveningMedicines.any { !it.isAccepted && !it.isSkipped })
                                                Canvas(
                                                    modifier = Modifier
                                                        .padding(start = 9.dp)
                                                        .size(12.dp)
                                                        .align(Alignment.TopEnd)
                                                ) {
                                                    drawCircle(
                                                        color = onErrorContainer,
                                                        center = center
                                                    )
                                                }
                                        }
                                    }
                                }
                            }
                            if (isAfterCurrentDate) {
                                if ((currentMedicines.currentMorningMedicines + currentMedicines.currentEveningMedicines).isEmpty())
                                    item {
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            Text(
                                                text = stringResource(id = R.string.no_data),
                                                style = MaterialTheme.typography.headlineLarge,
                                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                                modifier = Modifier
                                                    .align(Alignment.Center)
                                                    .padding(top = 90.dp)
                                            )
                                        }
                                    }
                                else {
                                    items(currentMedicines.currentMorningMedicines) { medicine ->
                                        MedicineCard(
                                            medicine = medicine,
                                            modifier = Modifier.padding(bottom = 8.dp),
                                            onClick = { onClickMedicine(medicine) },
                                            isSkipped = false,
                                            isAccepted = false,
                                            isMorning = true,
                                            isFuture = true
                                        )
                                    }
                                    items(currentMedicines.currentEveningMedicines) { medicine ->
                                        MedicineCard(
                                            medicine = medicine,
                                            modifier = Modifier.padding(bottom = 8.dp),
                                            onClick = { onClickMedicine(medicine) },
                                            isSkipped = false,
                                            isAccepted = false,
                                            isMorning = false,
                                            isFuture = true
                                        )
                                    }
                                }
                            } else if (isMorningMedicines) {
                                if (currentMedicines.currentMorningMedicines.isEmpty())
                                    item {
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            Text(
                                                text = stringResource(id = R.string.no_data),
                                                style = MaterialTheme.typography.headlineLarge,
                                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                                modifier = Modifier
                                                    .align(Alignment.Center)
                                                    .padding(top = 90.dp)
                                            )
                                        }
                                    }
                                else
                                    items(currentMedicines.currentMorningMedicines) { medicine ->
                                        MedicineCard(
                                            medicine = medicine,
                                            modifier = Modifier.padding(bottom = 8.dp),
                                            onClick = { onClickMedicine(medicine) },
                                            isSkipped = medicine.isSkipped,
                                            isAccepted = medicine.isAccepted,
                                            isMorning = true,
                                            isFuture = false
                                        )
                                    }
                            } else {
                                if (currentMedicines.currentEveningMedicines.isEmpty())
                                    item {
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            Text(
                                                text = stringResource(id = R.string.no_data),
                                                style = MaterialTheme.typography.headlineLarge,
                                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                                modifier = Modifier
                                                    .align(Alignment.Center)
                                                    .padding(top = 90.dp)
                                            )
                                        }
                                    }
                                else
                                    items(currentMedicines.currentEveningMedicines) { medicine ->
                                        MedicineCard(
                                            medicine = medicine,
                                            modifier = Modifier.padding(bottom = 8.dp),
                                            onClick = { onClickMedicine(medicine) },
                                            isSkipped = medicine.isSkipped,
                                            isAccepted = medicine.isAccepted,
                                            isMorning = false,
                                            isFuture = false
                                        )
                                    }
                            }
                        }
                    }
                }
            }
        }
        FloatingButton(
            onClick = { onNavigate(NewMedicineScreen.route) },
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@Composable
fun FloatingButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .padding(16.dp)
            .size(56.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            tint = MaterialTheme.colorScheme.tertiaryContainer,
            modifier = Modifier.size(24.dp),
            contentDescription = null
        )
    }
}

@Composable
fun MedicineCard(
    modifier: Modifier = Modifier,
    medicine: MedicineCard,
    onClick: () -> Unit,
    isSkipped: Boolean,
    isAccepted: Boolean,
    isMorning: Boolean,
    isFuture: Boolean
) {
    val cornerRadius = 16.dp
    var parentWidthPx by remember { mutableIntStateOf(0) }
    val parentWidthDp: Dp = with(LocalDensity.current) {
        parentWidthPx.toDp()
    }

    Box(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .drawBehind {
            val color =
                if (isAccepted) onPrimaryContainer
                else if (isSkipped) onErrorContainer
                else Color.Transparent
            drawLine(
                color = color,
                start = Offset(x = 0f, y = cornerRadius.toPx() + 3),
                end = Offset(
                    x = 0f,
                    y = ((size.height - 16).toDp() + cornerRadius).toPx() - cornerRadius.toPx() * 2 - 3
                ),
                strokeWidth = 6.dp.toPx()
            )
            drawArc(
                color = color,
                startAngle = 180f,
                sweepAngle = -90f,
                useCenter = false,
                topLeft = Offset(
                    x = 0f,
                    y = ((size.height - 16).toDp() + cornerRadius).toPx() - cornerRadius.toPx() * 3 - 6
                ),
                size = Size(cornerRadius.toPx() * 2, cornerRadius.toPx() * 2),
                style = Stroke(width = 6.dp.toPx())
            )
            drawArc(
                color = color,
                startAngle = 180f,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(x = 0f, y = 6f),
                size = Size(cornerRadius.toPx() * 2, cornerRadius.toPx() * 2),
                style = Stroke(width = 6.dp.toPx())
            )
        }
    ) {
        Card(
            elevation = 0.dp,
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 112.dp, max = 136.dp)
                .clickable { if (!isSkipped && !isAccepted && !isFuture) onClick() }
                .alpha(if (isFuture) 0.3f else 1.0f),
            shape = RoundedCornerShape(cornerRadius)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp)
                .onGloballyPositioned { coordinates ->
                    parentWidthPx = coordinates.size.height
                }
            ) {
                Image(
                    painter = painterResource(
                        id =
                        if (isSkipped || isAccepted) R.drawable.medicine_transparent
                        else R.drawable.medicines_example
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(0.30f)
                        .height(parentWidthDp)
                )
                Column(
                    modifier = Modifier
                        .weight(0.70f)
                        .padding(start = 16.dp, top = 12.dp, end = 16.dp)
                        .alpha(if (isSkipped || isAccepted) 0.3f else 1f)
                ) {
                    Text(
                        text = medicine.name,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onError
                    )
                    Text(
                        text = medicine.undername,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onError
                    )
                    Text(
                        text = medicine.dose,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onError
                    )
                    Row(modifier = Modifier.padding(top = 16.dp)) {
                        if (!isMorning) Icon(
                            painter = painterResource(id = R.drawable.moon_icon),
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp, bottom = 12.dp),
                            tint = MaterialTheme.colorScheme.onError
                        )
                        else Icon(
                            painter = painterResource(id = R.drawable.sun_icon),
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp, bottom = 12.dp),
                            tint = MaterialTheme.colorScheme.onError
                        )
                        Text(
                            text = medicine.frequency,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(bottom = 12.dp),
                            color = MaterialTheme.colorScheme.onError
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DaysOfWeekHeader(daysOfWeek: List<DayOfWeek>) {
    Row {
        daysOfWeek.forEach { dayOfWeek ->
            Text(
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 5.dp)
                    .weight(1f)
                    .wrapContentHeight(),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
fun WeekHeader(weekState: WeekState, onClickWeek: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { weekState.currentWeek = weekState.currentWeek.dec() },
            modifier = Modifier.size(26.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null,
            )
        }
        Text(
            text = (weekState.currentWeek.yearMonth.month.getDisplayName(
                TextStyle.FULL_STANDALONE, Locale.getDefault()
            ) + " " + weekState.currentWeek.yearMonth.year).replaceFirstChar { it.titlecase() },
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.weight(1f))
        Card(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(end = 16.dp)
                .size(width = 90.dp, height = 24.dp)
                .clickable { onClickWeek(false) },
            border = BorderStroke(
                width = 2.dp, color = MaterialTheme.colorScheme.secondary
            ),
            backgroundColor = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.week_chart),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        IconButton(
            onClick = { weekState.currentWeek = weekState.currentWeek.inc() },
            modifier = Modifier.size(26.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null,
            )
        }
    }
}

@Composable
fun DayContent(
    state: DayState<DynamicSelectionState>,
    getAmountNotAcceptedMedicines: suspend (LocalDate) -> Int
) {
    var currentColorCircle by remember { mutableStateOf(secondary) }

    val date = state.date
    val selectionState = state.selectionState
    val isSelected = selectionState.isDateSelected(date)

    // вынесли выполнение условий в корутину, чтобы не блокировать UI
    LaunchedEffect(key1 = Unit) {
        currentColorCircle = withContext(Dispatchers.IO) {
            if (date.isAfter(LocalDate.now())) secondary
            else if (getAmountNotAcceptedMedicines(date) > 0) onErrorContainer
            else onPrimaryContainer
        }
    }

    Box(
        modifier = Modifier
            .padding(5.dp)
            .aspectRatio(0.9f)
    ) {
        Card(modifier = Modifier
            .aspectRatio(1f)
            .clickable { selectionState.onDateSelected(date) }
            .align(Alignment.TopCenter),
            shape = MaterialTheme.shapes.small,
            border = null,
            backgroundColor =
            if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.errorContainer
        ) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    color =
                    if (isSelected) MaterialTheme.colorScheme.tertiaryContainer
                    else if (state.isCurrentDay) MaterialTheme.colorScheme.primary
                    else if (state.isFromCurrentMonth) MaterialTheme.colorScheme.onError
                    else MaterialTheme.colorScheme.surfaceVariant,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
        Canvas(
            modifier = Modifier
                .size(8.dp)
                .align(Alignment.BottomCenter)
        ) {
            drawCircle(
                color = currentColorCircle, center = center
            )
        }
    }
}

@Composable
fun MonthHeader(monthState: MonthState, onClickMonth: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                monthState.currentMonth = monthState.currentMonth.minusMonths(1)
            }, modifier = Modifier.size(26.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null,
            )
        }
        Text(
            text = (monthState.currentMonth.month.getDisplayName(
                TextStyle.FULL_STANDALONE, Locale.getDefault()
            ) + " " + monthState.currentMonth.year).replaceFirstChar { it.titlecase() },
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.weight(1f))
        Card(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(end = 16.dp)
                .size(width = 90.dp, height = 24.dp)
                .clickable { onClickMonth(true) },
            border = BorderStroke(
                width = 2.dp, color = MaterialTheme.colorScheme.secondary
            ),
            backgroundColor = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.month_chart),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        IconButton(
            onClick = {
                monthState.currentMonth = monthState.currentMonth.plusMonths(1)
            }, modifier = Modifier.size(26.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = null,
            )
        }
    }
}

@Composable
fun CalendarItem(
    modifier: Modifier = Modifier,
    onDateChanged: (LocalDate) -> Boolean,
    currentDate: LocalDate,
    weekDates: Week,
    getAmountNotAcceptedMedicines: suspend (LocalDate) -> Int,
    isWeek: Boolean,
    therapyEvent: (TherapyEvent) -> Unit
) {
    // внутри ViewModel currentDate обновляется так, чтобы не триггерить recomposition календаря снова
    if (isWeek) SelectableWeekCalendar(modifier = modifier,
        calendarState = rememberSelectableWeekCalendarState(initialSelection = listOf(currentDate),
            initialWeek = weekDates,
            confirmSelectionChange = { if (it.isNotEmpty()) onDateChanged(it.first()) else false }),
        dayContent = { state ->
            DayContent(state = state, getAmountNotAcceptedMedicines = getAmountNotAcceptedMedicines)
        },
        daysOfWeekHeader = { daysOfWeek ->
            DaysOfWeekHeader(daysOfWeek = daysOfWeek)
        },
        weekHeader = { weekState ->
            WeekHeader(
                weekState = weekState,
                onClickWeek = { therapyEvent(TherapyEvent.OnChangeIsWeek(it)) })
        }
    )
    else SelectableCalendar(modifier = modifier,
        calendarState = rememberSelectableCalendarState(initialSelection = listOf(currentDate),
            initialMonth = YearMonth.from(currentDate),
            confirmSelectionChange = { if (it.isNotEmpty()) onDateChanged(it.first()) else false }),
        dayContent = { state ->
            DayContent(state = state, getAmountNotAcceptedMedicines = getAmountNotAcceptedMedicines)
        },
        monthHeader = { monthState ->
            MonthHeader(monthState = monthState, onClickMonth = {
                therapyEvent(TherapyEvent.OnChangeIsWeek(it))
                therapyEvent(TherapyEvent.OnGetWeekDates(currentDate))
            })
        },
        daysOfWeekHeader = { daysOfWeek ->
            DaysOfWeekHeader(daysOfWeek = daysOfWeek)
        }
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewTherapyScreen() {
    AesculapiusTheme {
        TherapyScreen(
            getAmountNotAcceptedMedicines = { 30 },
            therapyEvent = {},
            currentDate = LocalDate.now(),
            updateCurrentDate = { true },
            currentLoadingState = CurrentLoadingState.Success(
                TherapyUiState(
                    currentMorningMedicines = listOf(
                        MedicineCard(
                            id = 12,
                            name = "препарат",
                            undername = "подпрепарат",
                            dose = "12мг/доза",
                            frequency = "2 дозы",
                            isSkipped = false,
                            isAccepted = true,
                            doseId = 12,
                            endDate = LocalDate.now(),
                            startDate = LocalDate.now(),
                            fullFrequency = "2 дозы 2 раза в день",
                            medicineType = CurrentMedicineType.Aerosol
                        ),
                        MedicineCard(
                            id = 12,
                            name = "длинный препарат препарат",
                            undername = "подпрепарат",
                            dose = "12мг/доза",
                            frequency = "2 дозы",
                            isSkipped = true,
                            isAccepted = false,
                            doseId = 12,
                            endDate = LocalDate.now(),
                            startDate = LocalDate.now(),
                            fullFrequency = "2 дозы 2 раза в день",
                            medicineType = CurrentMedicineType.Aerosol
                        )
                    )
                )
            ),
            generalLoadingState = GeneralLoadingState.Success,
            currentWeekDates = Week.now(),
            onNavigate = {},
            isAfterCurrentDate = false,
            isWeek = true,
            onClickMedicine = {},
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}