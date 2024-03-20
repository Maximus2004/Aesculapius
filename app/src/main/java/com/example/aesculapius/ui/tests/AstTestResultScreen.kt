package com.example.aesculapius.ui.tests

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aesculapius.ui.TopBar
import com.example.aesculapius.ui.navigation.NavigationDestination
import com.example.aesculapius.ui.theme.Roboto

object ASTTestResult : NavigationDestination {
    override val route = "ASTTestResult"
}

@Composable
fun ASTTestResultScreen(
    onNavigateBack: () -> Unit,
    onClickReturnButton: () -> Unit,
    summaryScore: Int
) {
    val textASTResultGood = "У тебя не было симптомов астмы и связанных с ней ограничений. Если ситуация изменится – проконсультируйся с врачом."
    val textASTResultMedium = "За последние 4 недели ты хорошо контролировал астму, но не полностью. Твой врач поможет добиться полного контроля."
    val textASTResultBad = "За последние 4 недели тебе не удавалось контролировать астму. Твой врач может посоветовать, какие меры нужно принять, чтобы добиться более полного контроля над астмой."
    val textASTGoodResult = buildAnnotatedString {
        append("Сумма ")
        withStyle(
            style = SpanStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.W500,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        ) {
            append("25 быллов")
        }
        append(" - ты полностью контролировал астму за последние 4 недели.")
    }
    val textASTMediumResult = buildAnnotatedString {
        append("Сумма ")
        withStyle(
            style = SpanStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.W500,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        ) {
            append("20-24 былла")
        }
        append(" - ты у цели.")
    }
    val textASTBadResult = buildAnnotatedString {
        append("Сумма ")
        withStyle(
            style = SpanStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.W500,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        ) {
            append("19 баллов и меньше")
        }
        append(" - мимо цели.")
    }

    Scaffold(topBar = {
        TopBar(text = "АСТ тестирование", onNavigateBack = { onNavigateBack() }) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding(), end = 24.dp, start = 24.dp)
        ) {
            Text(
                text = "Ваш результат:",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(bottom = 8.dp, top = 10.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "$summaryScore баллов",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = if (summaryScore == 25) textASTResultGood else if (summaryScore in 20..24) textASTResultMedium else textASTResultBad,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 40.dp),
            )
            Text(
                text = "Пояснение", style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "Шкала ACT включает в себя 5 пунктов для самостоятельного заполнения пациентом, каждому пункту присваивается значение от 1 до 5 баллов, которые затем суммируются (общее значение шкалы - 5 - 25 баллов).",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(text = textASTGoodResult, style = MaterialTheme.typography.headlineMedium)
            Text(
                text = textASTMediumResult,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(text = textASTBadResult, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { onClickReturnButton() },
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .height(56.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Вернуться в раздел",
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}