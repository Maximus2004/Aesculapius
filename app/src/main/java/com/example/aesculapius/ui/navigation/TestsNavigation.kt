package com.example.aesculapius.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aesculapius.ui.tests.TestsScreen
import com.example.aesculapius.ui.tests.TestsViewModel

@Composable
fun TestsNavigation(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {
    val testsViewModel: TestsViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = TestsScreen.route,
        modifier = modifier.fillMaxSize()
    ) {
        composable(route = TestsScreen.route) {
            TestsScreen()
        }
    }
}