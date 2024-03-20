package com.example.aesculapius.ui.tests

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.aesculapius.ui.TopBar
import com.example.aesculapius.ui.signup.TextInput

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MetricsTestScreen(
    onClickDoneButton: (first: Float, second: Float, third: Float) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequesterSecondMetrics = remember { FocusRequester() }
    val focusRequesterThirdMetrics = remember { FocusRequester() }

    var firstMetrics by remember { mutableStateOf("") }
    var secondMetrics by remember { mutableStateOf("") }
    var thirdMetrics by remember { mutableStateOf("") }

    BackHandler { onNavigateBack() }
    Scaffold(topBar = {
        TopBar(
            onNavigateBack = { onNavigateBack() },
            text = "Ввод метрик",
            existHelpButton = true,
            onClickHelpButton = { /* TODO */ })
    }) { paddingValues ->
        Column(
            modifier = modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = 24.dp,
                    end = 24.dp
                )
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Введите метрики с Вашего\n пикфлоуметра",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp, bottom = 40.dp),
                textAlign = TextAlign.Center
            )
            TextInput(
                text = firstMetrics,
                onValueChanged = { firstMetrics = it },
                hint = "Первая метрика, л/мин",
                modifier = Modifier.padding(bottom = 24.dp).fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(onNext = { focusRequesterSecondMetrics.requestFocus() }),
                focusRequester = FocusRequester()
            )

            TextInput(
                text = secondMetrics,
                onValueChanged = { secondMetrics = it },
                hint = "Вторая метрика, л/мин",
                modifier = Modifier.padding(bottom = 24.dp).fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(onNext = { focusRequesterThirdMetrics.requestFocus() }),
                focusRequester = focusRequesterSecondMetrics
            )

            TextInput(
                text = thirdMetrics,
                onValueChanged = { thirdMetrics = it },
                hint = "Третья метрика, л/мин",
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                focusRequester = focusRequesterThirdMetrics
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    try {
                        val temp1 = firstMetrics.toFloat()
                        val temp2 = secondMetrics.toFloat()
                        val temp3 = thirdMetrics.toFloat()
                        if (temp1 < 0 || temp2 < 0 || temp3 < 0)
                            throw IllegalArgumentException("Неверный формат метрик")
                        else
                            onClickDoneButton(temp1, temp2, temp3)
                    } catch (e: NumberFormatException) {
                        Toast.makeText(context, "Введите корректные числа", Toast.LENGTH_SHORT)
                            .show()
                    } catch (e: IllegalArgumentException) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .height(56.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Отправить данные",
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}