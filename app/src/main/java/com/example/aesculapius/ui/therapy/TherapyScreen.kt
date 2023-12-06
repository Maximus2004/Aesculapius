package com.example.aesculapius.ui.therapy

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aesculapius.data.Medicine
import com.example.aesculapius.data.medicines
import com.example.aesculapius.data.medicinesActive
import com.example.aesculapius.ui.home.HomeViewModel
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TherapyScreen(modifier: Modifier = Modifier) {
    val therapyViewModel: TherapyViewModel = viewModel()
    val currentMedicines = therapyViewModel.currentMedicines.collectAsState().value

    val currentWeekDates = therapyViewModel.currentWeekDates.collectAsState().value

    // не так важно, чтобы отдельно выносить во viewModel (в каком виде выводятся лекарства)
    var isActiveMedicines by remember { mutableStateOf(true) }
    Column(modifier = modifier.fillMaxSize()) {
        CalendarItem(
            currentDate = therapyViewModel.getCurrentDate(),
            onDateChanged = { therapyViewModel.updateCurrentDate(it) },
            getActiveAmount = { therapyViewModel.getAmountActive(it) },
            weekDates = currentWeekDates,
            getWeekDates = { therapyViewModel.getWeekDates(it) }
        )
        Card(
            elevation = 0.dp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 29.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                Text(text = "Ежедневный прогресс", style = MaterialTheme.typography.bodyLarge)
                LinearProgressIndicator(
                    progress = currentMedicines.progress,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 10.dp)
                        .height(6.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Выполнено ${currentMedicines.done} из ${currentMedicines.amount}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "${(currentMedicines.progress * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        Row(modifier = Modifier.padding(bottom = 24.dp)) {
            Button(
                onClick = { isActiveMedicines = true },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isActiveMedicines) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                    contentColor = if (isActiveMedicines) Color.White else MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Активные", style = MaterialTheme.typography.headlineSmall)
            }
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = { isActiveMedicines = false },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isActiveMedicines) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                    contentColor = if (!isActiveMedicines) Color.White else MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Завершённые", style = MaterialTheme.typography.headlineSmall)
            }
        }
        LazyColumn() {
            if (isActiveMedicines)
                items(currentMedicines.currentActiveMedicines) { medicine ->
                    MedicineCard(medicine = medicine, modifier = Modifier.padding(bottom = 16.dp))
                }
            else
                items(currentMedicines.currentEndedMedicines) { medicine ->
                    MedicineCard(medicine = medicine, modifier = Modifier.padding(bottom = 16.dp))
                }
        }
    }
}

@Composable
fun MedicineCard(modifier: Modifier = Modifier, medicine: Medicine) {
    Card(
        elevation = 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(108.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 10.dp)
        ) {
            Image(
                painter = painterResource(id = medicine.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.weight(0.30f)
            )
            Text(
                text = medicine.name,
                modifier = Modifier
                    .weight(0.70f)
                    .padding(start = 16.dp, top = 12.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun DaysOfWeekHeader(daysOfWeek: List<DayOfWeek>) {
    Row() {
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
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null,
            )
        }
        Text(
            text = (weekState.currentWeek.yearMonth.month
                .getDisplayName(
                    TextStyle.FULL_STANDALONE,
                    Locale.getDefault()
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
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            backgroundColor = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "неделя",
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
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null,
            )
        }
    }
}

@Composable
fun DayContent(
    state: DayState<DynamicSelectionState>,
    activeAmount: Int,
) {
    var currentColorCircle by remember { mutableStateOf(Color(0xFFB0A3D1)) }

    val date = state.date
    val selectionState = state.selectionState
    val isSelected = selectionState.isDateSelected(date)

    // вынесли выполнение условий в корутину, чтобы не блокировать UI
    LaunchedEffect(key1 = Unit) {
        currentColorCircle = withContext(Dispatchers.Main) {
            if (state.date.isAfter(LocalDate.now())) Color(0xFFB0A3D1)
            else if (activeAmount > 0) Color(0xFFFC3B69)
            else Color(0xFF9ED209)
        }
    }

    Box(
        modifier = Modifier
            .padding(5.dp)
            .aspectRatio(0.9f)
    ) {
        Card(
            modifier = Modifier
                .aspectRatio(1f)
                .clickable { selectionState.onDateSelected(date) }
                .align(Alignment.TopCenter),
            shape = MaterialTheme.shapes.small,
            border = null,
            backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color(
                0xFFE3E0EA
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    color =
                    if (isSelected) Color.White
                    else if (state.isCurrentDay) Color(0xFF6750A4)
                    else if (state.isFromCurrentMonth) Color.Black
                    else Color(0xFF86818B),
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
                color = currentColorCircle,
                center = center
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
            },
            modifier = Modifier.size(26.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null,
            )
        }
        Text(
            text = (monthState.currentMonth.month.getDisplayName(
                TextStyle.FULL_STANDALONE,
                Locale.getDefault()
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
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            backgroundColor = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "месяц",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        IconButton(
            onClick = {
                monthState.currentMonth = monthState.currentMonth.plusMonths(1)
            },
            modifier = Modifier.size(26.dp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null,
            )
        }
    }
}

@Composable
fun CalendarItem(
    modifier: Modifier = Modifier,
    onDateChanged: (LocalDate) -> Boolean,
    getActiveAmount: (DayState<DynamicSelectionState>) -> Int,
    currentDate: LocalDate,
    weekDates: Week,
    getWeekDates: (LocalDate) -> Unit
) {
    var isWeek by remember { mutableStateOf(true) }
//    анимасьон
//    Column(
//        modifier = Modifier.animateContentSize(
//            animationSpec = spring(
//                dampingRatio = Spring.DampingRatioLowBouncy,
//                stiffness = Spring.StiffnessLow
//            )
//        )
//    ) {
    if (isWeek)
        SelectableWeekCalendar(
            modifier = modifier,
            calendarState = rememberSelectableWeekCalendarState(
                initialSelection = listOf(currentDate),
                initialWeek = weekDates,
                confirmSelectionChange = { if (it.isNotEmpty()) onDateChanged(it.first()) else false }
            ),
            dayContent = { state ->
                DayContent(state = state, activeAmount = getActiveAmount(state))
            },
            daysOfWeekHeader = { daysOfWeek ->
                DaysOfWeekHeader(daysOfWeek = daysOfWeek)
            },
            weekHeader = { weekState ->
                WeekHeader(weekState = weekState, onClickWeek = { isWeek = it })
            }
        )
    else
        SelectableCalendar(
            modifier = modifier,
            calendarState = rememberSelectableCalendarState(
                initialSelection = listOf(currentDate),
                initialMonth = YearMonth.from(currentDate),
                confirmSelectionChange = { if (it.isNotEmpty()) onDateChanged(it.first()) else false }
            ),
            dayContent = { state ->
                DayContent(state = state, activeAmount = getActiveAmount(state))
            },
            monthHeader = { monthState ->
                MonthHeader(monthState = monthState, onClickMonth = {
                    isWeek = it
                    getWeekDates(currentDate)
                })
            },
            daysOfWeekHeader = { daysOfWeek ->
                DaysOfWeekHeader(daysOfWeek = daysOfWeek)
            }
        )
//    }
}
