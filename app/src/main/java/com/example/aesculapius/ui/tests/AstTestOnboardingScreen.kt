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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.aesculapius.R
import com.example.aesculapius.ui.TopBar
import com.example.aesculapius.ui.navigation.NavigationDestination

object AstTestOnboardingScreen : NavigationDestination {
    override val route = "ASTTestOnboardingScreen"
}

@Composable
fun AstTestOnboardingScreen(
    onNavigateBack: () -> Unit,
    onClickBeginButton: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(topBar = {
        TopBar(
            text = stringResource(id = R.string.ast_test_name),
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
                text = stringResource(R.string.test_control_asthma),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(176.dp)
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(R.string.ast_test_onboarding),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = stringResource(R.string.test_is_easy),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(top = 40.dp, bottom = 8.dp),
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(R.string.asthma_control), style = MaterialTheme.typography.headlineMedium
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
                    text = stringResource(id = R.string.begin),
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}