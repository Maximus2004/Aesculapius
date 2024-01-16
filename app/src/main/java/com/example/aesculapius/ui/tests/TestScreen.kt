package com.example.aesculapius.ui.tests

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.RadioButton
import androidx.compose.material.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.aesculapius.ui.TopBar
import com.example.aesculapius.ui.navigation.NavigationDestination

object TestScreen : NavigationDestination {
    override val route = "TestScreen"
    const val depart = "departure"
    val routeWithArgs: String = "$route/{$depart}"
}

@Composable
fun TestScreen(
    testName: String,
    questionsList: MutableList<Question>,
    onNavigateBack: () -> Unit,
    onClickSummary: (Int) -> Unit,
    turnOffBars: () -> Unit,
) {
    var isAlertDialogShown by remember { mutableStateOf(false) }
    var currentPage by remember { mutableIntStateOf(0) }
    val currentAnswers by remember { mutableStateOf(MutableList(questionsList.size) { -1 }) }
    var currentAnswer by remember { mutableIntStateOf(-1) }
    val context = LocalContext.current

    BackHandler { onNavigateBack() }
    LaunchedEffect(key1 = Unit) { turnOffBars() }

    Scaffold(topBar = {
        TopBar(
            onNavigateBack = { isAlertDialogShown = true },
            text = testName,
            existHelpButton = true,
            onClickHelpButton = { /* TODO */ }
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding(),
                start = 24.dp,
                end = 24.dp
            )
        ) {
            LazyRow(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .height(37.dp)
            ) {
                items(questionsList.size) { index ->
                    Box(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .aspectRatio(0.9f)
                    ) {
                        Card(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable {
                                    currentAnswers[currentPage] = currentAnswer
                                    currentPage = index
                                    if (currentAnswers[index] != -1)
                                        currentAnswer = currentAnswers[index]
                                    else currentAnswer = -1
                                }
                                .align(Alignment.TopCenter),
                            shape = MaterialTheme.shapes.small,
                            backgroundColor =
                            if (currentAnswers[index] != -1) MaterialTheme.colorScheme.primary
                            else Color(0xFFE3E0EA)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (index + 1).toString(),
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = if (currentAnswers[index] != -1) Color.White else Color.Black
                                )
                            }
                        }
                        if (index == currentPage) {
                            Canvas(
                                modifier = Modifier
                                    .size(8.dp)
                                    .align(Alignment.BottomCenter)
                            ) {
                                drawCircle(color = Color(0xFFEEECF0), center = center)
                            }
                        }
                    }
                }
            }
            Text(
                text = questionsList[currentPage].questionText,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 29.dp, bottom = 18.dp),
                color = Color.Black
            )
            LazyColumn() {
                itemsIndexed(questionsList[currentPage].answersList) { index, answer ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {
                        RadioButton(
                            selected = (index == currentAnswer),
                            onClick = { currentAnswer = index }
                        )
                        Text(
                            text = answer,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 17.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    if (currentPage == questionsList.size - 1) {
                        if (-1 in currentAnswers && currentAnswer == -1) {
                            Toast.makeText(context, "Ответьте на все вопросы", Toast.LENGTH_SHORT).show()
                            currentAnswers[currentPage] = currentAnswer
                        }
                        else {
                            currentAnswers[currentPage] = currentAnswer
                            onClickSummary(currentAnswers.sum() + currentAnswers.size)
                        }
                    } else {
                        currentAnswers[currentPage] = currentAnswer
                        currentPage++
                        currentAnswer =
                            if (currentAnswers[currentPage] != -1) currentAnswers[currentPage] else -1
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
                    text = if (currentPage == questionsList.size - 1) "Подвести итоги" else "Дальше",
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center
                )
            }
            if (isAlertDialogShown) {
                AlertDialog(
                    onDismissRequest = { isAlertDialogShown = false },
                    modifier = Modifier
                        .wrapContentHeight()
                        .width(315.dp),
                    shape = RoundedCornerShape(16.dp),
                    title = {
                        Text(
                            text = "Вы уверены, что хотите выйти?",
                            style = MaterialTheme.typography.displayMedium,
                            modifier = Modifier.padding(top = 34.dp)
                        )
                    },
                    text = {
                        Text(
                            text = "Ваш текущий прогресс не сохранится",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    dismissButton = {
                        TextButton(onClick = { onNavigateBack() }, modifier = Modifier.padding(bottom = 24.dp)) {
                            Text(
                                text = "Выйти", style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { isAlertDialogShown = false }, modifier = Modifier.padding(bottom = 24.dp, end = 24.dp)) {
                            Text(
                                text = "Назад", style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        }
    }
}