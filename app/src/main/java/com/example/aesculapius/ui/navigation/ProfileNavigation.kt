package com.example.aesculapius.ui.navigation

import android.util.Log
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
import androidx.navigation.navigation
import com.example.aesculapius.data.Hours
import com.example.aesculapius.ui.profile.EditProfileScreen
import com.example.aesculapius.ui.profile.LearnItemScreen
import com.example.aesculapius.ui.profile.LearnScreen
import com.example.aesculapius.ui.profile.ProfileScreen
import com.example.aesculapius.ui.profile.SetReminderTimeProfile
import com.example.aesculapius.ui.signup.SetReminderTime
import com.example.aesculapius.ui.signup.SignUpUiState
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ProfileNavigation(
    morningReminder: LocalDateTime,
    eveningReminder: LocalDateTime,
    turnOffBars: () -> Unit,
    turnOnBars: () -> Unit,
    saveMorningReminder: (LocalDateTime) -> Unit,
    saveEveningReminder: (LocalDateTime) -> Unit,
    user: SignUpUiState,
    onSaveNewUser: (SignUpUiState) -> Unit,
    navController: NavHostController
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
                onClickSetReminder = { navController.navigate(SetReminderTimeProfile.route) },
                onClickLearnBlock = { navController.navigate(LearnScreen.route) },
                onClickProfileBlock = { navController.navigate(EditProfileScreen.route) }
            )
        }
        composable(route = SetReminderTimeProfile.route) {
            SetReminderTimeProfile(
                morningTime = morningReminder,
                eveningTime = eveningReminder,
                onNavigateBack = {
                    navController.navigateUp()
                    turnOnBars()
                },
                onClickSetReminder = { navController.navigate("${SetReminderTime.route}/${it}") },
                turnOffBars = turnOffBars
            )
        }
        composable(route = EditProfileScreen.route) {
            EditProfileScreen(
                turnOffBars = turnOffBars,
                onNavigateBack = {
                    navController.navigateUp()
                    turnOnBars()
                },
                user = user,
                onSaveNewUser = onSaveNewUser
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
                        if (!(it.hour in 5..12 && eveningReminder.hour in 18 .. 23))
                            Toast.makeText(context, "Утреннее измерение не может проводиться позже 12:00, а вечернее недоступно раньше 18:00", Toast.LENGTH_SHORT).show()
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
                        if (!(morningReminder.hour in 5..12 && it.hour in 18 .. 23))
                            Toast.makeText(context, "Утреннее измерение не может проводиться позже 12:00, а вечернее недоступно раньше 18:00", Toast.LENGTH_SHORT).show()
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
        composable(route = LearnScreen.route) {
            LearnScreen(
                turnOffBars = turnOffBars,
                onNavigateBack = {
                    navController.navigateUp()
                    turnOnBars()
                },
                onClickItem = { name, text -> navController.navigate("${LearnItemScreen.route}/$name^$text") }
            )
        }
        composable(
            route = LearnItemScreen.routeWithArgs,
            arguments = listOf(navArgument(name = LearnItemScreen.depart) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val arg = backStackEntry.arguments?.getString(LearnItemScreen.depart)?.split('^') ?: listOf()
            LearnItemScreen(onNavigateBack = { navController.navigateUp() }, name = arg[0], text = arg[1])
        }
    }
}