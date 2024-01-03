package com.example.aesculapius.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aesculapius.data.Hours
import com.example.aesculapius.ui.home.HomeScreen
import com.example.aesculapius.ui.start.OnboardingScreen
import com.example.aesculapius.ui.start.SetReminderTime
import com.example.aesculapius.ui.start.SignUpScreen
import com.example.aesculapius.ui.start.SignUpViewModel
import java.time.format.DateTimeFormatter

@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {
    val signUpViewModel: SignUpViewModel = viewModel()
    val signUpUiState = signUpViewModel.uiStateSingUp.collectAsState().value
    var currentPage by remember { mutableIntStateOf(0) }

    NavHost(
        navController = navController,
        startDestination = OnboardingScreen.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(route = OnboardingScreen.route) {
            OnboardingScreen(onClickEnd = { navController.navigate(SignUpScreen.route) })
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
                    textHours = signUpUiState.reminderMorning.format(DateTimeFormatter.ofPattern("HH")),
                    textMinutes = signUpUiState.reminderMorning.format(DateTimeFormatter.ofPattern("mm")),
                    onDoneButton = {
                        signUpViewModel.onMorningTimeChanged(it)
                        navController.navigateUp()
                    },
                    onNavigateBack = { navController.navigateUp() }
                )

                Hours.Evening -> SetReminderTime(
                    title = "Вечернее напоминание",
                    textHours = signUpUiState.reminderEvening.format(DateTimeFormatter.ofPattern("HH")),
                    textMinutes = signUpUiState.reminderEvening.format(DateTimeFormatter.ofPattern("mm")),
                    onDoneButton = {
                        signUpViewModel.onEveningTimeChanged(it)
                        navController.navigateUp()
                    },
                    onNavigateBack = { navController.navigateUp() }
                )
            }
        }
        composable(route = SignUpScreen.route) {
            SignUpScreen(
                name = signUpUiState.name,
                surname = signUpUiState.surname,
                patronymic = signUpUiState.patronymic,
                height = signUpUiState.height,
                weight = signUpUiState.weight,
                eveningTime = signUpUiState.reminderEvening,
                morningTime = signUpUiState.reminderMorning,
                currentPage = currentPage,
                onChangeCurrentPage = { currentPage++ },
                onNameChanged = { signUpViewModel.onNameChanged(it) },
                onSurnameChanged = { signUpViewModel.onSurnameChanged(it) },
                onChangedPatronymic = { signUpViewModel.onChangedPatronymic(it) },
                onDateChanged = { signUpViewModel.onDateChanged(it) },
                onHeightChanged = { signUpViewModel.onHeightChanged(it) },
                onWeightChanged = { signUpViewModel.onWeightChanged(it) },
                onEndRegistration = {
                    navController.navigate(HomeScreen.route) {
                        popUpTo(OnboardingScreen.route) { inclusive = true }
                    }
                },
                onClickSetReminder = { navController.navigate("${SetReminderTime.route}/${it}") }
            )
        }
        composable(route = HomeScreen.route) {
            HomeScreen()
        }
    }
}