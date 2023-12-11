package com.example.aesculapius.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aesculapius.ui.home.HomeScreen
import com.example.aesculapius.ui.navigation.StartNavigation

// TODO make review
@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = StartNavigation.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = StartNavigation.route) {
            StartNavigation(onEndRegistration = { navController.navigate(HomeScreen.route) })
        }
        composable(route = HomeScreen.route) {
            HomeScreen()
        }
    }
}