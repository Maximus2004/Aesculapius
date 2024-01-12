package com.example.aesculapius.ui.tests

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aesculapius.ui.TopBar
import com.example.aesculapius.ui.navigation.NavigationDestination
import com.example.aesculapius.ui.theme.AesculapiusTheme

object MetricsOnboardingScreen : NavigationDestination {
    override val route = "TestScreen"
}

@Composable
fun MetricsOnboardingScreen(
    onNavigateBack: () -> Unit,
    onClickBeginButton: () -> Unit,
    turnOffBars: () -> Unit
) {
    val textFirst = buildAnnotatedString {
        append("Ввод метрик с пикфлоуметра - ")
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.W500,
                color = MaterialTheme.colorScheme.primary
            )
        ) {
            append("важный шаг")
        }
        append(" на пути к лучшему управлению вашей астмой. С вашей активной участием мы сможем вместе сделать каждый день ")
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.W500,
                color = MaterialTheme.colorScheme.primary
            )
        ) {
            append("легче и здоровее.")
        }
    }

    val textSecond = buildAnnotatedString {
        append("Чтобы обеспечить ")
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.W500,
                color = MaterialTheme.colorScheme.primary
            )
        ) {
            append("максимальную")
        }
        append(" точность, придерживайтесь ")
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.W500,
                color = MaterialTheme.colorScheme.primary
            )
        ) {
            append("установленного графика")
        }
        append(" измерений. Результаты будут автоматически сохранены в нашей базе данных для вашего удобства и последующего анализа.")
    }

    LaunchedEffect(key1 = Unit) { turnOffBars() }

    Scaffold(topBar = {
        TopBar(onNavigateBack = { onNavigateBack() }, text = "Ввод метрик")
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = 24.dp,
                    end = 24.dp
                )
                .fillMaxWidth()
        ) {
            Text(
                text = "Ввод показаний\n с пикфлоуметра",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp, bottom = 24.dp)
            )
            Text(text = textFirst, style = MaterialTheme.typography.headlineMedium)
            Text(
                text = "Точность и последовательность",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 40.dp, bottom = 8.dp)
            )
            Text(text = textSecond, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { onClickBeginButton() },
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .height(56.dp)
                    .fillMaxWidth()
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

@Preview(showBackground = true)
@Composable
fun MetricsPreview() {
    AesculapiusTheme {
        MetricsOnboardingScreen(onNavigateBack = {}, onClickBeginButton = {}, turnOffBars = {})
    }
}