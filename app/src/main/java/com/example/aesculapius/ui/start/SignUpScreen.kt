package com.example.aesculapius.ui.start

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.ListItemPicker
import com.example.aesculapius.data.Hours
import com.example.aesculapius.data.days
import com.example.aesculapius.data.months
import com.example.aesculapius.ui.navigation.NavigationDestination
import com.example.aesculapius.ui.theme.AesculapiusTheme
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object SignUpScreen : NavigationDestination {
    override val route = "SignUpScreen"
}

@Composable
fun SignUpScreen(
    name: String,
    surname: String,
    patronymic: String,
    height: String,
    weight: String,
    currentPage: Int,
    onChangeCurrentPage: () -> Unit,
    morningTime: LocalTime,
    eveningTime: LocalTime,
    onNameChanged: (String) -> Unit,
    onSurnameChanged: (String) -> Unit,
    onChangedPatronymic: (String) -> Unit,
    onDateChanged: (LocalDate) -> Unit,
    onHeightChanged: (String) -> Unit,
    onWeightChanged: (String) -> Unit,
    onClickSetReminder: (Hours) -> Unit,
    onEndRegistration: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
    ) {
        DotsMenuSignUp(
            totalDots = 4,
            selectedIndex = currentPage,
            Modifier.padding(top = 26.dp, bottom = 24.dp)
        )
        when (currentPage) {
            0 -> FieldsFIO(
                name = name,
                surname = surname,
                patronymic = patronymic,
                onNameChanged = { onNameChanged(it) },
                onSurnameChanged = { onSurnameChanged(it) },
                onChangedPatronymic = { onChangedPatronymic(it) }
            )
            1 -> BirthdayFiled(onDateChanged = { onDateChanged(it) })
            2 -> HeightWeightFields(
                onHeightChanged = { onHeightChanged(it) },
                onWeightChanged = { onWeightChanged(it) },
                height = height,
                weight = weight
            )
            3 -> ReminderFields(
                onClickSetReminder = { onClickSetReminder(it) },
                eveningTime = eveningTime,
                morningTime = morningTime
            )
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = { if (currentPage == 3) onEndRegistration() else onChangeCurrentPage() },
            enabled = if (currentPage == 0) name != "" && surname != "" && patronymic != "" else if (currentPage == 2) height != "" && weight != "" else true,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .size(height = 56.dp, width = 312.dp),
            colors = ButtonDefaults.buttonColors(disabledContainerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(text = "Дальше", style = MaterialTheme.typography.displaySmall)
        }
    }
}

@Composable
fun ReminderFields(
    onClickSetReminder: (Hours) -> Unit,
    eveningTime: LocalTime,
    morningTime: LocalTime
) {
    val textPlanFirst = buildAnnotatedString {
        append("Выберите время для напоминаний, которое ")
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.W500,
                color = MaterialTheme.colorScheme.primary
            )
        ) {
            append("наилучшим образом")
        }
        append(" соответствует Вашему режиму дня и позволяет выполнять измерения точно и без спешки. Это поможет Вам оставаться на пути к лучшему здоровью и более точному контролю над Вашей астмой.")
    }

    val textPlanSecond = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.W500,
                color = MaterialTheme.colorScheme.primary
            )
        ) {
            append("Утро и вечер")
        }
        append(" - два ключевых момента для мониторинга вашего состояния.")
    }
    Text(
        text = "Настройте время напоминаний для ввода метрик с пикфлоуметра",
        style = MaterialTheme.typography.titleMedium,
        textAlign = TextAlign.Center
    )

    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 24.dp, bottom = 16.dp)
            .clickable { onClickSetReminder(Hours.Morning) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row() {
            Column(modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 16.dp)) {
                Text(
                    text = "Утро",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = morningTime.format(DateTimeFormatter.ofPattern("HH")) + " : "
                            + morningTime.format(DateTimeFormatter.ofPattern("mm")),
                    style = MaterialTheme.typography.displayMedium
                )
            }
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 28.dp)
                    .size(25.dp)
            )
        }
    }
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClickSetReminder(Hours.Evening) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row() {
            Column(modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 16.dp)) {
                Text(
                    text = "Вечер",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = eveningTime.format(DateTimeFormatter.ofPattern("HH")) + " : "
                            + eveningTime.format(DateTimeFormatter.ofPattern("mm")),
                    style = MaterialTheme.typography.displayMedium
                )
            }
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 28.dp)
                    .size(25.dp)
            )
        }
    }

    Text(
        text = "Планирование Вашего дня",
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(top = 40.dp, bottom = 8.dp),
        color = MaterialTheme.colorScheme.primary,
    )
    Text(text = textPlanFirst, style = MaterialTheme.typography.headlineMedium)
    Text(text = textPlanSecond, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 8.dp))
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HeightWeightFields(
    onHeightChanged: (String) -> Unit,
    onWeightChanged: (String) -> Unit,
    height: String,
    weight: String
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Text(text = "Укажите свой рост, см", style = MaterialTheme.typography.titleMedium)
    TextInput(
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        text = height,
        onValueChanged = { onHeightChanged(it) },
        hint = "Рост",
        focusRequester = FocusRequester(),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        modifier = Modifier.padding(bottom = 48.dp)
    )
    Text(text = "Укажите свой вес, кг", style = MaterialTheme.typography.titleMedium)
    TextInput(
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        text = weight,
        onValueChanged = { onWeightChanged(it) },
        hint = "Вес",
        focusRequester = FocusRequester(),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
    )
}

@Composable
fun BirthdayFiled(onDateChanged: (LocalDate) -> Unit) {
    Text(text = "Укажите дату рождения", style = MaterialTheme.typography.titleMedium)
    var day by remember { mutableStateOf(1) }
    var month by remember { mutableStateOf("янв") }
    var year by remember { mutableStateOf(2000) }
    Row(modifier = Modifier.padding(top = 24.dp)) {
        Card(
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .width(96.dp)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            ListItemPicker(
                modifier = Modifier.fillMaxWidth(),
                value = day,
                list = (1..days[month]!!).toList(),
                onValueChange = {
                    day = it
                    onDateChanged(LocalDate.of(year, months.indexOf(month) + 1, day))
                },
                textStyle = MaterialTheme.typography.displayMedium,
                dividersColor = MaterialTheme.colorScheme.primary
            )
        }
        Card(
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .wrapContentHeight()
                .padding(horizontal = 12.dp)
                .width(96.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            ListItemPicker(
                modifier = Modifier.fillMaxWidth(),
                value = month,
                list = months,
                onValueChange = {
                    month = it
                    onDateChanged(LocalDate.of(year, months.indexOf(month) + 1, day))
                },
                textStyle = MaterialTheme.typography.displayMedium,
                dividersColor = MaterialTheme.colorScheme.primary
            )
        }
        Card(
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .width(96.dp)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            ListItemPicker(
                modifier = Modifier.fillMaxWidth(),
                value = year,
                list = (1900..2023).toList(),
                onValueChange = {
                    year = it
                    onDateChanged(LocalDate.of(year, months.indexOf(month) + 1, day))
                },
                textStyle = MaterialTheme.typography.displayMedium,
                dividersColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FieldsFIO(
    name: String,
    surname: String,
    patronymic: String,
    onNameChanged: (String) -> Unit,
    onSurnameChanged: (String) -> Unit,
    onChangedPatronymic: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequesterName = remember { FocusRequester() }
    val focusRequesterPatronymic = remember { FocusRequester() }

    Text(text = "Укажите своё имя", style = MaterialTheme.typography.titleMedium)
    TextInput(
        text = name,
        onValueChanged = { onNameChanged(it) },
        hint = "Фамилия",
        modifier = Modifier.padding(top = 24.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { focusRequesterName.requestFocus() }),
        focusRequester = FocusRequester()
    )
    TextInput(
        text = surname,
        onValueChanged = { onSurnameChanged(it) },
        hint = "Имя",
        modifier = Modifier.padding(top = 24.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        focusRequester = focusRequesterName,
        keyboardActions = KeyboardActions(onNext = { focusRequesterPatronymic.requestFocus() }),
    )
    TextInput(
        text = patronymic,
        onValueChanged = { onChangedPatronymic(it) },
        hint = "Отчество",
        modifier = Modifier.padding(top = 24.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        focusRequester = focusRequesterPatronymic,
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
    )
}


@Composable
fun TextInput(
    keyboardOptions: KeyboardOptions,
    text: String,
    onValueChanged: (String) -> Unit,
    hint: String,
    focusRequester: FocusRequester,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = text,
        label = { Text(text = hint, color = Color.Gray) },
        onValueChange = { onValueChanged(it) },
        trailingIcon = {
            IconButton(onClick = { onValueChanged("") }) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color(0xFF49454F)
                )
            }
        },
        singleLine = true,
        modifier = modifier.focusRequester(focusRequester),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@Composable
fun DotsMenuSignUp(totalDots: Int, selectedIndex: Int, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center
    ) {
        items(totalDots) { index ->
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(color = if (index == selectedIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
            )
            if (index != totalDots - 1) Spacer(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    AesculapiusTheme {

    }
}