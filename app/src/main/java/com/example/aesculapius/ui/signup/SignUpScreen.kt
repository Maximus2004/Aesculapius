package com.example.aesculapius.ui.signup

import android.widget.Toast
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.ListItemPicker
import com.example.aesculapius.data.Hours
import com.example.aesculapius.data.days
import com.example.aesculapius.data.daysSpecial
import com.example.aesculapius.data.months
import com.example.aesculapius.ui.navigation.NavigationDestination
import com.example.aesculapius.ui.theme.AesculapiusTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object SignUpScreen : NavigationDestination {
    override val route = "SignUpScreen"
}

@Composable
fun SignUpScreen(
    onChangeCurrentPage: () -> Unit,
    userUiState: SignUpUiState,
    currentPage: Int,
    onEvent: (SignUpEvent) -> Unit,
    onClickSetReminder: (Hours) -> Unit,
    onEndRegistration: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
                name = userUiState.name,
                surname = userUiState.surname,
                patronymic = userUiState.patronymic,
                onEvent = onEvent
            )
            1 -> BirthdayFiled(onEvent = onEvent)
            2 -> HeightWeightFields(
                onEvent = onEvent,
                height = userUiState.height,
                weight = userUiState.weight
            )
            3 -> ReminderFields(
                onClickSetReminder = { onClickSetReminder(it) },
                eveningTime = userUiState.eveningReminder,
                morningTime = userUiState.morningReminder,
                modifier = Modifier.align(Alignment.Start)
            )
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = {
                when (currentPage) {
                    3 -> onEndRegistration()
                    2 -> {
                        try {
                            val heightFinal = userUiState.height.toFloat()
                            val weightFinal = userUiState.weight.toFloat()
                            if (!(heightFinal in 20f..300f && weightFinal in 0f..1000f))
                                throw IllegalArgumentException("Неверный формат веса или роста")
                            else {
                                onChangeCurrentPage()
                            }
                        } catch (e: NumberFormatException) {
                            Toast.makeText(context, "Введите корректные числа", Toast.LENGTH_SHORT)
                                .show()
                        } catch (e: IllegalArgumentException) {
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    0 -> {
                        val regex = Regex("[а-яА-Яa-zA-Z]+")
                        if (regex.matches(userUiState.name) && regex.matches(userUiState.surname) && regex.matches(userUiState.patronymic))
                            onChangeCurrentPage()
                        else
                            Toast.makeText(context, "Введите корректные данные", Toast.LENGTH_SHORT).show()
                    }
                    else -> onChangeCurrentPage()
                }
            },
            enabled = if (currentPage == 0) userUiState.name != "" && userUiState.surname != "" else if (currentPage == 2) userUiState.height != "" && userUiState.weight != "" else true,
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
    eveningTime: LocalDateTime,
    morningTime: LocalDateTime,
    modifier: Modifier = Modifier
) {
    val textPlanFirst =
        "Проводить пикфлоуметрию нужно 2 раза в день - утром и вечером!\n" +
        "Лучше выбрать время, когда ты сможешь выполнить пикфлоуметрию без спешки. \n" +
        "Лучше выбрать одно и то же время утром и вечером (например, 9.00 и 21.00), но это не обязательно.\n" +
        "Запиши результат!"

    Text(
        text = "Настрой время напоминаний для пикфлоуметрии",
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
        text = "Планирование твоего дня",
        style = MaterialTheme.typography.headlineLarge,
        modifier = modifier.padding(top = 40.dp, bottom = 8.dp),
        color = MaterialTheme.colorScheme.primary,
    )
    Text(text = textPlanFirst, style = MaterialTheme.typography.headlineMedium)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HeightWeightFields(
    onEvent: (SignUpEvent) -> Unit,
    height: String,
    weight: String
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Text(text = "Твой рост, см", style = MaterialTheme.typography.titleMedium)
    TextInput(
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        text = height,
        onValueChanged = { onEvent(SignUpEvent.OnHeightChanged(it)) },
        hint = "Рост",
        focusRequester = FocusRequester(),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        modifier = Modifier.padding(bottom = 48.dp)
    )
    Text(text = "Твой вес, кг", style = MaterialTheme.typography.titleMedium)
    TextInput(
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        text = weight,
        onValueChanged = { onEvent(SignUpEvent.OnWeightChanged(it)) },
        hint = "Вес",
        focusRequester = FocusRequester(),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
    )
}

@Composable
fun BirthdayFiled(onEvent: (SignUpEvent) -> Unit) {
    Text(text = "Укажите дату рождения", style = MaterialTheme.typography.titleMedium)
    var day by remember { mutableIntStateOf(1) }
    var month by remember { mutableStateOf("янв") }
    var year by remember { mutableIntStateOf(2000) }
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
                list = if (year % 4 != 0) (1..days[month]!!).toList() else (1..daysSpecial[month]!!).toList(),
                onValueChange = {
                    day = it
                    onEvent(SignUpEvent.OnBirthdayChanged(LocalDate.of(year, months.indexOf(month) + 1, day)))
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
                    onEvent(SignUpEvent.OnBirthdayChanged(LocalDate.of(year, months.indexOf(month) + 1, day)))
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
                    onEvent(SignUpEvent.OnBirthdayChanged(LocalDate.of(year, months.indexOf(month) + 1, day)))
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
    onEvent: (SignUpEvent) -> Unit,
    name: String,
    surname: String,
    patronymic: String
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequesterName = remember { FocusRequester() }
    val focusRequesterPatronymic = remember { FocusRequester() }

    Text(text = "Твои фамилия, имя, отчество", style = MaterialTheme.typography.titleMedium)
    TextInput(
        text = surname,
        onValueChanged = { onEvent(SignUpEvent.OnSurnameChanged(it)) },
        hint = "Фамилия",
        modifier = Modifier.padding(top = 24.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { focusRequesterName.requestFocus() }),
        focusRequester = FocusRequester()
    )
    TextInput(
        text = name,
        onValueChanged = { onEvent(SignUpEvent.OnNameChanged(it)) },
        hint = "Имя",
        modifier = Modifier.padding(top = 24.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        focusRequester = focusRequesterName,
        keyboardActions = KeyboardActions(onNext = { focusRequesterPatronymic.requestFocus() }),
    )
    TextInput(
        text = patronymic,
        onValueChanged = { onEvent(SignUpEvent.OnPatronymicChanged(it)) },
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
            if (text != "")
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
        SignUpScreen(
            onChangeCurrentPage = {},
            userUiState = SignUpUiState(),
            currentPage = 1,
            onEvent = {},
            onClickSetReminder = {},
            onEndRegistration = {}
        )
    }
}