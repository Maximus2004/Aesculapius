package com.example.aesculapius.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.aesculapius.data.Hours
import com.example.aesculapius.ui.profile.EditProfileScreen
import com.example.aesculapius.ui.profile.LearnItemScreen
import com.example.aesculapius.ui.profile.LearnScreen
import com.example.aesculapius.ui.profile.ProfileEvent
import com.example.aesculapius.ui.profile.ProfileScreen
import com.example.aesculapius.ui.profile.SetReminderTimeProfile
import com.example.aesculapius.ui.signup.SetReminderTime
import com.example.aesculapius.ui.signup.SignUpUiState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun NavGraphBuilder.profileNavGraph(
    userUiState: SignUpUiState,
    turnOffBars: () -> Unit,
    turnOnBars: () -> Unit,
    onProfileEvent: (ProfileEvent) -> Unit,
    navController: NavHostController,
) {
    composable(route = ProfileScreen.route) {
        ProfileScreen(
            modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp),
            onClickSetReminder = { navController.navigate(SetReminderTimeProfile.route) },
            onClickLearnBlock = { navController.navigate(LearnScreen.route) },
            onClickProfileBlock = { navController.navigate(EditProfileScreen.route) }
        )
        turnOnBars()
    }
    composable(route = SetReminderTimeProfile.route) {
        SetReminderTimeProfile(
            morningTime = userUiState.morningReminder,
            eveningTime = userUiState.eveningReminder,
            onNavigateBack = { navController.navigateUp() },
            onClickSetReminder = { navController.navigate("${SetReminderTime.route}/${it}") },
        )
        turnOffBars()
    }
    composable(route = EditProfileScreen.route) {
        EditProfileScreen(
            onNavigateBack = { navController.navigateUp() },
            user = userUiState,
            onSaveNewUser = { onProfileEvent(ProfileEvent.OnSaveNewUser(it)) }
        )
        turnOffBars()
    }
    composable(
        route = SetReminderTime.routeWithArgs,
        arguments = listOf(navArgument(name = SetReminderTime.depart) {
            type = NavType.StringType
        })
    ) { backStackEntry ->
        val context = LocalContext.current
        val arg = Hours.valueOf(
            backStackEntry.arguments?.getString(SetReminderTime.depart) ?: "Morning"
        )
        when (arg) {
            Hours.Morning -> SetReminderTime(
                title = "Утреннее напоминание",
                textHours = userUiState.morningReminder.format(DateTimeFormatter.ofPattern("HH")),
                textMinutes = userUiState.morningReminder.format(DateTimeFormatter.ofPattern("mm")),
                onDoneButton = {
                    if (userUiState.eveningReminder.hour - it.hour < 8)
                        Toast.makeText(context, "Между утренним и вечерним напоминанием должно быть минимум 8 часов", Toast.LENGTH_LONG).show()
                    else {
                        onProfileEvent(ProfileEvent.OnSaveMorningTime(it))
                        navController.navigateUp()
                    }
                },
                onNavigateBack = { navController.navigateUp() },
                textTopBar = "Настройка напоминаний"
            )

            Hours.Evening -> SetReminderTime(
                title = "Вечернее напоминание",
                textHours = userUiState.eveningReminder.format(DateTimeFormatter.ofPattern("HH")),
                textMinutes = userUiState.eveningReminder.format(DateTimeFormatter.ofPattern("mm")),
                onDoneButton = {
                    if (it.hour - userUiState.morningReminder.hour < 8)
                        Toast.makeText(context, "Между утренним и вечерним напоминанием должно быть минимум 8 часов", Toast.LENGTH_LONG).show()
                    else {
                        onProfileEvent(ProfileEvent.OnSaveEveningTime(it))
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
            onNavigateBack = {
                navController.navigateUp()
                turnOnBars()
            },
            onClickItem = { name, text -> navController.navigate("${LearnItemScreen.route}/$name^$text") }
        )
        turnOffBars()
    }
    composable(
        route = LearnItemScreen.routeWithArgs,
        arguments = listOf(navArgument(name = LearnItemScreen.depart) {
            type = NavType.StringType
        })
    ) { backStackEntry ->
        val arg =
            backStackEntry.arguments?.getString(LearnItemScreen.depart)?.split('^') ?: listOf()
        LearnItemScreen(
            onNavigateBack = { navController.navigateUp() },
            name = arg[0],
            text = arg[1]
        )
    }
}