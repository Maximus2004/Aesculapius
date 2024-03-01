package com.example.aesculapius.ui.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.example.aesculapius.ui.statistics.StatisticsScreen
import com.example.aesculapius.ui.statistics.StatisticsViewModel
import androidx.navigation.compose.composable

fun NavGraphBuilder.statisticsNavGraph(
    statisticsViewModel: StatisticsViewModel,
    modifier: Modifier = Modifier
) {
    composable(route = StatisticsScreen.route) {
        StatisticsScreen(statisticsViewModel = statisticsViewModel, modifier = modifier)
    }
}