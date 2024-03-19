package com.example.aesculapius.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aesculapius.data.learnList
import com.example.aesculapius.ui.TopBar
import com.example.aesculapius.ui.navigation.NavigationDestination
import com.example.aesculapius.ui.theme.AesculapiusTheme

object LearnScreen : NavigationDestination {
    override val route = "LearnScreen"
}

@Composable
fun LearnScreen(
    onNavigateBack: () -> Unit,
    onClickItem: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(topBar = {
        TopBar(
            onNavigateBack = onNavigateBack,
            existHelpButton = false,
            text = "Обучающий блок"
        )
    }) { paddingValues ->
        LazyColumn(
            modifier = modifier.padding(
                top = paddingValues.calculateTopPadding() + 16.dp,
                start = 24.dp,
                end = 24.dp
            )
        ) {
            items(learnList) { listItem ->
                Card(
                    elevation = 0.dp,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 16.dp)
                        .clickable { onClickItem(listItem.name, listItem.text) }) {
                    Text(
                        text = listItem.name,
                        Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LearnScreenPreview() {
    AesculapiusTheme {
        LearnScreen(
            onClickItem = {  _, _ -> },
            onNavigateBack = {}
        )
    }
}