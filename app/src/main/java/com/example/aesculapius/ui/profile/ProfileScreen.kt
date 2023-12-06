package com.example.aesculapius.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aesculapius.R
import com.example.aesculapius.ui.theme.AesculapiusTheme

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .clickable { /* TODO */ }
                .fillMaxWidth(),
            elevation = 0.dp
        ) {
            SingleItem(image = R.drawable.profile_icon, name = "Профиль")
        }
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .clickable { /* TODO */ }
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            elevation = 0.dp
        ) {
            SingleItem(image = R.drawable.book_icon, name = "Обучающий блок")
        }
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .clickable { /* TODO */ }
                .fillMaxWidth(),
            elevation = 0.dp
        ) {
            Column {
                SingleItem(image = R.drawable.timer_icon, name = "Настройка напоминаний")
                Divider(
                    color = MaterialTheme.colorScheme.secondary,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                SingleItem(image = R.drawable.theme_icon, name = "Цветовая тема")
                Divider(
                    color = MaterialTheme.colorScheme.secondary,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                SingleItem(image = R.drawable.question_icon, name = "Связь с разработчиком")

            }
        }
    }
}

@Composable
fun SingleItem(image: Int, name: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)
    ) {
        Image(painter = painterResource(id = image), contentDescription = null)
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    AesculapiusTheme {
        ProfileScreen()
    }
}