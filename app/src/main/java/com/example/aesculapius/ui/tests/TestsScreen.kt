package com.example.aesculapius.ui.tests

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.aesculapius.R
import com.example.aesculapius.ui.navigation.NavigationDestination

object TestsScreen : NavigationDestination {
    override val route = "TestsScreen"
}

@Composable
fun TestsScreen(
    onClickRecTest: () -> Unit,
    onClickASTTest: () -> Unit,
    onClickMetricsTest: () -> Unit,
    turnOnBars: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(key1 = Unit) { turnOnBars() }

    LazyColumn(modifier = modifier) {
        item {
            Text(
                text = "Тесты и метрики",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(bottom = 24.dp, top = 10.dp)
                    .fillMaxWidth()
            )
            Text(
                text = "Своевременный ввод метрик и регулярное прохождение тестирований предоставляют уникальную возможность глубокого понимания Вашего текущего состояния.",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 40.dp, start = 8.dp, end = 8.dp)
            )
            Box() {
                Card(
                    elevation = 0.dp,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Ввод метрик с пикфлоуметра",
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.padding(bottom = 4.dp),
                            color = Color.Black
                        )
                        Text(
                            text = "Контролируйте состояние своих легких для лучшего здоровья",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.time_icon),
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp, top = 8.dp)
                            )
                            Text(
                                text = "Время пришло!",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Spacer(Modifier.weight(1f))
                            Card(
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(81.dp)
                                    .clickable { onClickMetricsTest() },
                                shape = RoundedCornerShape(12.dp),
                                backgroundColor = MaterialTheme.colorScheme.primary
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Начать",
                                        style = MaterialTheme.typography.headlineMedium,
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
                Canvas(
                    modifier = Modifier
                        .padding(top = 13.dp, start = 9.dp)
                        .size(16.dp)
                        .align(Alignment.TopEnd)
                ) {
                    drawCircle(
                        color = Color(0xFFFC3B69),
                        center = center
                    )
                }
            }
            Box {
                Card(
                    elevation = 0.dp,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 16.dp)
                        .clickable { onClickRecTest() }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Тест приверженности",
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.padding(bottom = 4.dp),
                            color = Color.Black
                        )
                        Text(
                            text = "Оцените свою лечебную дисциплину - это важный шаг к успешному контролю над астмой",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.time_icon),
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp, top = 8.dp)
                            )
                            Text(
                                text = "Время пришло!",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Spacer(Modifier.weight(1f))
                            Card(
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(81.dp),
                                shape = RoundedCornerShape(12.dp),
                                backgroundColor = MaterialTheme.colorScheme.primary
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Начать",
                                        style = MaterialTheme.typography.headlineMedium,
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
                Canvas(
                    modifier = Modifier
                        .padding(top = 13.dp, start = 9.dp)
                        .size(16.dp)
                        .align(Alignment.TopEnd)
                ) {
                    drawCircle(
                        color = Color(0xFFFC3B69),
                        center = center
                    )
                }
            }

            Box {
                Card(
                    elevation = 0.dp,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 16.dp, bottom = 20.dp)
                        .clickable { onClickASTTest() }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "АСТ тестирование",
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.padding(bottom = 4.dp),
                            color = Color.Black
                        )
                        Text(
                            text = "Проходите ежемесячное тестирование для более качественного понимания Вашей астмы",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.time_icon),
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp, top = 8.dp)
                            )
                            Text(
                                text = "Время пришло!",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Spacer(Modifier.weight(1f))
                            Card(
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(81.dp),
                                shape = RoundedCornerShape(12.dp),
                                backgroundColor = MaterialTheme.colorScheme.primary
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Начать",
                                        style = MaterialTheme.typography.headlineMedium,
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
                Canvas(
                    modifier = Modifier
                        .padding(top = 13.dp, start = 9.dp)
                        .size(16.dp)
                        .align(Alignment.TopEnd)
                ) {
                    drawCircle(
                        color = Color(0xFFFC3B69),
                        center = center
                    )
                }
            }
        }
    }
}

//Card(
//modifier = Modifier
//.height(40.dp)
//.width(106.dp),
//shape = RoundedCornerShape(12.dp),
//backgroundColor = MaterialTheme.colorScheme.secondary
//) {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "Недоступно",
//            style = MaterialTheme.typography.headlineMedium,
//            textAlign = TextAlign.Center,
//            color = Color.White
//        )
//    }
//}