package com.example.aesculapius.ui.profile

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.aesculapius.ui.TopBar
import com.example.aesculapius.ui.navigation.NavigationDestination
import com.example.aesculapius.ui.signup.SignUpUiState
import com.example.aesculapius.ui.signup.TextInput
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

object EditProfileScreen : NavigationDestination {
    override val route = "EditProfileScreen"
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditProfileScreen(turnOffBars: () -> Unit, onNavigateBack: () -> Unit, user: SignUpUiState, onSaveNewUser: (SignUpUiState) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    BackHandler { onNavigateBack() }
    LaunchedEffect(key1 = Unit) { turnOffBars() }

    var tempUser by remember { mutableStateOf(user) }
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    var tempBirthday by remember { mutableStateOf(user.birthday.format(formatter)) }
    val pattern = Pattern.compile("^\\d{0,2}/\\d{0,2}/\\d{0,4}$")

    DisposableEffect(Unit) {
        onDispose {
            try {
                val newUser = tempUser.copy(birthday = LocalDate.parse(tempBirthday, formatter))
                if (!(tempUser.height.toFloat() in 1f..500f && tempUser.weight.toFloat() in 0f..999f))
                    throw IllegalArgumentException("Неверный формат веса или роста")
                else {
                    onSaveNewUser(newUser)
                    Toast.makeText(context, "Данные успешно сохранены", Toast.LENGTH_SHORT).show()
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "Введены некорректные числа", Toast.LENGTH_SHORT).show()
            } catch (e: IllegalArgumentException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(topBar = { TopBar(text = "Профиль", existHelpButton = true, onNavigateBack = onNavigateBack) }) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(top = paddingValues.calculateTopPadding(), start = 24.dp, end = 24.dp, bottom = 10.dp)) {
            item {
                val keyboardController = LocalSoftwareKeyboardController.current

                TextInput(
                    text = tempUser.surname,
                    onValueChanged = { tempUser = tempUser.copy(surname = it) },
                    hint = "Фамилия",
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    focusRequester = FocusRequester()
                )
                TextInput(
                    text = tempUser.name,
                    onValueChanged = { tempUser = tempUser.copy(name = it) },
                    hint = "Имя",
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    focusRequester = FocusRequester()
                )
                TextInput(
                    text = tempUser.patronymic,
                    onValueChanged = { tempUser = tempUser.copy(patronymic = it) },
                    hint = "Отчество",
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    focusRequester = FocusRequester()
                )

                TextInput(
                    text = tempBirthday,
                    onValueChanged = {
                        val matcher = pattern.matcher(it)
                        if (matcher.find()) tempBirthday = matcher.group()
                    },
                    hint = "Дата рождения, дд/мм/гггг",
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    focusRequester = FocusRequester()
                )

                TextInput(
                    text = tempUser.height,
                    onValueChanged = { tempUser = tempUser.copy(height = it) },
                    hint = "Рост, см",
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    focusRequester = FocusRequester()
                )
                TextInput(
                    text = tempUser.weight,
                    onValueChanged = { tempUser = tempUser.copy(weight = it) },
                    hint = "Вес, кг",
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    focusRequester = FocusRequester()
                )
            }
        }
    }
}