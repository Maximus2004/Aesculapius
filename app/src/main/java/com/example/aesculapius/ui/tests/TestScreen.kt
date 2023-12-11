package com.example.aesculapius.ui.tests

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.RadioButton
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.aesculapius.ui.TopBar

@Composable
fun Test(testName: String, questionsList: MutableList<Question>) {
    var currentPage by remember { mutableIntStateOf(0) }
    val currentAnswers by remember { mutableStateOf(MutableList(questionsList.size) { -1 }) }
    var currentAnswer by remember { mutableIntStateOf(-1) }
    Scaffold(topBar = {
        TopBar(
            onNavigateBack = { /*TODO*/ },
            text = testName
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
                                    if (currentAnswers[index] != -1) currentAnswer = currentAnswers[index] else currentAnswer = -1
                                }
                                .align(Alignment.TopCenter),
                            shape = MaterialTheme.shapes.small,
                            backgroundColor =
                            if (index == currentPage) MaterialTheme.colorScheme.primary
                            else Color(0xFFE3E0EA)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (index + 1).toString(),
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = if (index == currentPage) Color.White else Color.Black
                                )
                            }
                        }
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
            Text(
                text = questionsList[currentPage].questionText,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 35.dp, bottom = 18.dp),
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
                    currentAnswers[currentPage] = currentAnswer
                    currentPage++
                    currentAnswer = if (currentAnswers[currentPage] != -1) currentAnswers[currentPage] else -1
                },
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .height(56.dp)
                    .width(312.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (currentPage == questionsList.size - 1) "Подвести итоги" else "Дальше",
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}