package com.example.aesculapius.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aesculapius.R
import com.example.aesculapius.data.Hours
import com.example.aesculapius.ui.TopBar
import com.example.aesculapius.ui.navigation.NavigationDestination
import com.example.aesculapius.ui.theme.AesculapiusTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object SetReminderTimeProfile : NavigationDestination {
    override val route = "SetReminderTimeProfile"
}

@Composable
fun SetReminderTimeProfile(
    onClickSetReminder: (Hours) -> Unit,
    eveningTime: LocalDateTime,
    morningTime: LocalDateTime,
    onNavigateBack: () -> Unit
) {
    Scaffold(topBar = {
        TopBar(
            onNavigateBack = onNavigateBack,
            text = stringResource(id = R.string.set_reminders)
        )
    }) { paddingValue ->
        Column(
            Modifier.padding(top = paddingValue.calculateTopPadding() + 27.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 24.dp, bottom = 16.dp)
                    .clickable { onClickSetReminder(Hours.Morning) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row() {
                    Column(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 12.dp,
                            bottom = 16.dp
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.morning_text),
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = morningTime.format(DateTimeFormatter.ofPattern("HH")) + " : "
                                    + morningTime.format(DateTimeFormatter.ofPattern("mm")),
                            style = MaterialTheme.typography.displayMedium
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 28.dp)
                            .size(25.dp)
                    )
                }
            }
            Card(
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable { onClickSetReminder(Hours.Evening) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row() {
                    Column(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 12.dp,
                            bottom = 16.dp
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.evening_text),
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = eveningTime.format(DateTimeFormatter.ofPattern("HH")) + " : "
                                    + eveningTime.format(DateTimeFormatter.ofPattern("mm")),
                            style = MaterialTheme.typography.displayMedium
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 28.dp)
                            .size(25.dp)
                    )
                }
            }
            Text(
                text = stringResource(R.string.plan_your_day),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 8.dp)
                    .align(Alignment.Start),
                color = MaterialTheme.colorScheme.primary,
            )
            Text(text = stringResource(R.string.set_reminder_text), style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SetReminderTimeProfileScreenPreview() {
    AesculapiusTheme {
        SetReminderTimeProfile(
            onClickSetReminder = {},
            eveningTime = LocalDateTime.now(),
            morningTime = LocalDateTime.now(),
            onNavigateBack = {}
        )
    }
}
