package com.example.aesculapius.ui.tests

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.aesculapius.data.testsList
import com.example.aesculapius.ui.navigation.NavigationDestination

object TestsScreen : NavigationDestination {
    override val route = "TestsScreen"
}

@Composable
fun TestsScreen() {
    LazyColumn(modifier = Modifier.padding(horizontal = 20.dp)) {
        item {
            Text(
                text = "Тесты и метрики",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(vertical = 22.dp)
                    .fillMaxWidth()
            )
            Text(
                text = "Своевременный ввод метрик и регулярное прохождение тестирований предоставляют уникальную возможность глубокого понимания Вашего текущего состояния.",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 40.dp)
            )
        }
        items(testsList) {test ->

        }
    }

}