package com.example.aesculapius.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aesculapius.data.Hours
import com.example.aesculapius.ui.profile.ProfileScreen
import com.example.aesculapius.ui.profile.SetReminderTimeProfile
import com.example.aesculapius.ui.start.OnboardingScreen
import com.example.aesculapius.ui.start.SetReminderTime
import com.example.aesculapius.ui.start.SignUpScreen
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun ProfileNavigation(
    morningReminder: LocalDateTime,
    eveningReminder: LocalDateTime,
    turnOffBars: () -> Unit,
    turnOnBars: () -> Unit,
    saveMorningReminder: (LocalDateTime) -> Unit,
    saveEveningReminder: (LocalDateTime) -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = ProfileScreen.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = ProfileScreen.route) {
            ProfileScreen(
                modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp),
                turnOnBars = turnOnBars,
                onClickSetReminder = { navController.navigate(SetReminderTimeProfile.route) }
            )
        }
        composable(route = SetReminderTimeProfile.route) {
            SetReminderTimeProfile(
                morningTime = morningReminder,
                eveningTime = eveningReminder,
                onNavigateBack = { navController.navigateUp() },
                onClickSetReminder = { navController.navigate("${SetReminderTime.route}/${it}") },
                turnOffBars = turnOffBars
            )
        }
        composable(
            route = SetReminderTime.routeWithArgs,
            arguments = listOf(navArgument(name = SetReminderTime.depart) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val arg = Hours.valueOf(
                backStackEntry.arguments?.getString(SetReminderTime.depart) ?: "Morning"
            )
            when (arg) {
                Hours.Morning -> SetReminderTime(
                    title = "Утреннее напоминание",
                    textHours = morningReminder.format(DateTimeFormatter.ofPattern("HH")),
                    textMinutes = morningReminder.format(DateTimeFormatter.ofPattern("mm")),
                    onDoneButton = {
                        if (Duration.between(it, eveningReminder).toHours() < 8)
                            Toast.makeText(context, "Между измерениями должно быть минимум 8 часов", Toast.LENGTH_SHORT).show()
                        else {
                            saveMorningReminder(it)
                            navController.navigateUp()
                        }
                    },
                    onNavigateBack = { navController.navigateUp() },
                    textTopBar = "Настройка напоминаний"
                )

                Hours.Evening -> SetReminderTime(
                    title = "Вечернее напоминание",
                    textHours = eveningReminder.format(DateTimeFormatter.ofPattern("HH")),
                    textMinutes = eveningReminder.format(DateTimeFormatter.ofPattern("mm")),
                    onDoneButton = {
                        if (Duration.between(morningReminder, it).toHours() < 8)
                            Toast.makeText(context, "Между измерениями должно быть минимум 8 часов", Toast.LENGTH_SHORT).show()
                        else {
                            saveEveningReminder(it)
                            navController.navigateUp()
                        }
                    },
                    onNavigateBack = { navController.navigateUp() },
                    textTopBar = "Настройка напоминаний"
                )
            }
        }
    }
}