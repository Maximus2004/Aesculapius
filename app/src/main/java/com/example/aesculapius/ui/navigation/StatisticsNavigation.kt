package com.example.aesculapius.ui.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.example.aesculapius.ui.statistics.StatisticsScreen
import com.example.aesculapius.ui.statistics.StatisticsViewModel
import androidx.navigation.compose.composable
import com.example.aesculapius.ui.signup.SignUpUiState

fun NavGraphBuilder.statisticsNavGraph(
    userUiState: SignUpUiState,
    statisticsViewModel: StatisticsViewModel,
    modifier: Modifier = Modifier
) {
    composable(route = StatisticsScreen.route) {
        StatisticsScreen(userUiState = userUiState, statisticsViewModel = statisticsViewModel, modifier = modifier)
    }
}