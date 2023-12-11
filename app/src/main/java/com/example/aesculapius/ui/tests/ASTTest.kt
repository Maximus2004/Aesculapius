package com.example.aesculapius.ui.tests

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aesculapius.ui.TopBar
import com.example.aesculapius.ui.theme.Roboto

@Composable
fun ASTTestScreen(onNavigateBack: () -> Unit, modifier: Modifier = Modifier) {
    val textASTTest = buildAnnotatedString {
        append("Каждую неделю отвечайте на несколько вопросов в анкете. Это займет всего ")
        withStyle(
            style = SpanStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.W500,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        ) {
            append("пару минут")
        }
        append(" и поможет вам и вашему врачу лучше понимать вашу астму. Вопросы о вашем самочувствии, симптомах и образе жизни помогут сформировать полную картину")
        withStyle(
            style = SpanStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.W500,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        ) {
            append(" Вашего состояния")
        }
    }
    Scaffold(topBar = {
        TopBar(
            text = "АСТ тестирование",
            onNavigateBack = { onNavigateBack() })
    }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding() + 16.dp,
                    start = 24.dp,
                    end = 24.dp
                )
        ) {
            Text(
                text = "Тест по контролю над астмой",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(176.dp)
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Мы сравниваем ваши ответы с данными, полученными при использовании пикфлоуметра, для оценки Вашего состояния и эффективности подобранного плана лечения.",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Пройти тестирование просто",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 40.dp, bottom = 8.dp),
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = textASTTest, style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { },
                modifier = Modifier
                    .padding(bottom = 64.dp)
                    .height(56.dp)
                    .width(312.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Начать",
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}