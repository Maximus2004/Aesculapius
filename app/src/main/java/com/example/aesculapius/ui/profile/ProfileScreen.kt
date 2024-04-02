package com.example.aesculapius.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aesculapius.R
import com.example.aesculapius.ui.navigation.NavigationDestination
import com.example.aesculapius.ui.theme.AesculapiusTheme

object ProfileScreen : NavigationDestination {
    override val route = "ProfileScreen"
}

@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .clickable { onNavigate(EditProfileScreen.route) }
                .fillMaxWidth(),
            elevation = 0.dp
        ) {
            SingleItem(
                image = R.drawable.profile_icon,
                name = stringResource(id = R.string.profile),
                onClick = { onNavigate(EditProfileScreen.route) }
            )
        }
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigate(LearnScreen.route) }
                .padding(vertical = 24.dp),
            elevation = 0.dp
        ) {
            SingleItem(
                image = R.drawable.book_icon,
                name = stringResource(id = R.string.learn_block_name),
                onClick = { onNavigate(LearnScreen.route) }
            )
        }
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = 0.dp
        ) {
            Column {
                SingleItem(
                    image = R.drawable.timer_icon,
                    name = stringResource(id = R.string.set_reminders),
                    onClick = { onNavigate(SetReminderTimeProfile.route) }
                )
                Divider(
                    color = MaterialTheme.colorScheme.secondary,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                SingleItem(image = R.drawable.theme_icon, name = stringResource(R.string.color_theme))
                Divider(
                    color = MaterialTheme.colorScheme.secondary,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                SingleItem(image = R.drawable.question_icon, name = stringResource(R.string.connect_with_developer))
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.version_1_0),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 20.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun SingleItem(image: Int, name: String, onClick: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 20.dp)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Image(painter = painterResource(id = image), contentDescription = null)
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ProfileScreenPreview() {
    AesculapiusTheme {
        ProfileScreen(onNavigate = {})
    }
}