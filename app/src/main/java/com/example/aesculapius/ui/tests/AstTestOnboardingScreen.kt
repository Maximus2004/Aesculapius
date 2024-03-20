package com.example.aesculapius.ui.tests

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.aesculapius.ui.TopBar
import com.example.aesculapius.ui.navigation.NavigationDestination

object ASTTestOnboardingScreen : NavigationDestination {
    override val route = "ASTTestOnboardingScreen"
}

@Composable
fun ASTTestOnboardingScreen(
    onNavigateBack: () -> Unit,
    onClickBeginButton: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val textASTTest =
        "Проходи тест 1 раз в месяц, чтобы понять, насколько хорошо контролируется твоя астма.\n" +
        "Это займет всего пару минут!\n" +
        "Тест поможет врачу подобрать для тебя лучшее лечение."

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
                text = "Мы сравниваем твои ответы с данными, полученными при использовании пикфлоуметра, для оценки твоего состояния и эффективности подобранного плана лечения.",
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