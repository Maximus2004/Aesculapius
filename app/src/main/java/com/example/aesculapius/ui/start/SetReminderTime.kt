package com.example.aesculapius.ui.start

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import java.time.LocalTime
import androidx.compose.ui.platform.LocalContext
import com.example.aesculapius.ui.TopBar
import com.example.aesculapius.ui.navigation.NavigationDestination

object SetReminderTime : NavigationDestination {
    override val route = "SetReminderTime"
    const val depart = "departure"
    val routeWithArgs: String = "$route/{$depart}"
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SetReminderTime(
    title: String,
    textHours: String,
    textMinutes: String,
    onDoneButton: (LocalTime) -> Unit,
    onNavigateBack: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var minutes by remember { mutableStateOf(textMinutes) }
    var hours by remember { mutableStateOf(textHours) }
    val context = LocalContext.current
    Scaffold(topBar = {
        TopBar(
            onNavigateBack = onNavigateBack,
            text = "Время напомнинаний"
        )
    }) { paddingValue ->
        Column(
            Modifier.padding(top = paddingValue.calculateTopPadding() + 27.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Card(
                modifier = Modifier.padding(24.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Text(
                    text = "Введите удобное для Вас время",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(24.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Spacer(Modifier.weight(1f))
                    Column() {
                        TextInputTime(
                            text = hours,
                            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Number
                            ),
                            modifier = Modifier.width(83.dp),
                            onValueChanged = { hours = it }
                        )
                        Text(text = "Часы", style = MaterialTheme.typography.labelSmall)
                    }
                    Text(text = ":", fontSize = 57.sp, modifier = Modifier.padding(5.dp))
                    Column() {
                        TextInputTime(
                            text = minutes,
                            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Number
                            ),
                            modifier = Modifier.width(83.dp),
                            onValueChanged = { minutes = it }
                        )
                        Text(text = "Минуты", style = MaterialTheme.typography.labelSmall)
                    }
                    Spacer(Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onNavigateBack() }) {
                        Text(text = "Назад", fontSize = 14.sp)
                    }
                    TextButton(onClick = {
                        try {
                            val hoursFinal = hours.toInt()
                            val minutesFinal = minutes.toInt()
                            if (!(hoursFinal in 0..23 && minutesFinal in 0..59))
                                throw IllegalArgumentException("Неверный формат времени")
                            else
                                onDoneButton(LocalTime.of(hoursFinal, minutesFinal))
                        } catch (e: NumberFormatException) {
                            Toast.makeText(context, "Введите корректные числа", Toast.LENGTH_SHORT)
                                .show()
                        } catch (e: IllegalArgumentException) {
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Text(text = "Ок", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInputTime(
    text: String,
    keyboardOptions: KeyboardOptions,
    onValueChanged: (String) -> Unit,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = { onValueChanged(it) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        textStyle = MaterialTheme.typography.displayLarge,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedLabelColor = Color(0xFF6750A4),
            unfocusedLabelColor = Color(0xFFAB90F1)
        ),
        shape = MaterialTheme.shapes.small
    )
}