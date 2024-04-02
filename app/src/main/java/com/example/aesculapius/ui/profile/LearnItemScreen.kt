package com.example.aesculapius.ui.profile

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aesculapius.R
import com.example.aesculapius.ui.TopBar
import com.example.aesculapius.ui.navigation.NavigationDestination
import com.example.aesculapius.ui.theme.AesculapiusTheme

object LearnItemScreen : NavigationDestination {
    override val route = "LearnItemScreen"
    const val depart = "departure"
    val routeWithArgs: String = "$route/{$depart}"
}

@Composable
fun LearnItemScreen(onNavigateBack: () -> Unit, name: String, text: String) {
    Scaffold(topBar = {
        TopBar(
            onNavigateBack = onNavigateBack,
            text = stringResource(R.string.learn_block_name),
            existHelpButton = false
        )
    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding() + 16.dp, start = 24.dp, end = 24.dp, bottom = 10.dp)
        ) {
            item {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(text = text, style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LearnItemScreenPreview() {
    AesculapiusTheme {
        LearnItemScreen(
            onNavigateBack = {},
            name = "Рандомный вопрос",
            text = "Рандомный ответ на вопрос. И очень-очень длинный текст ааа."
        )
    }
}