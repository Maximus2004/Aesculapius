package com.example.aesculapius.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aesculapius.data.Hours
import com.example.aesculapius.ui.signup.SetReminderTime
import com.example.aesculapius.ui.signup.SignUpScreen
import com.example.aesculapius.ui.signup.SignUpUiState
import com.example.aesculapius.ui.signup.SignUpViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun SignUpNavigation(
    morningReminder: LocalDateTime,
    eveningReminder: LocalDateTime,
    saveMorningReminder: (LocalDateTime) -> Unit,
    saveEveningReminder: (LocalDateTime) -> Unit,
    onEndRegistration: (SignUpUiState) -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val signUpViewModel: SignUpViewModel = viewModel()
    val signUpUiState = signUpViewModel.uiStateSingUp.collectAsState().value
    var currentPage by remember { mutableIntStateOf(0) }

    NavHost(
        navController = navController,
        startDestination = SignUpScreen.route,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(
            route = SetReminderTime.routeWithArgs,
            arguments = listOf(navArgument(name = SetReminderTime.depart) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val arg = Hours.valueOf(
                backStackEntry.arguments?.getString(SetReminderTime.depart) ?: Hours.Morning.name
            )
            when (arg) {
                Hours.Morning -> SetReminderTime(
                    title = "Утреннее напоминание",
                    textHours = morningReminder.format(DateTimeFormatter.ofPattern("HH")),
                    textMinutes = morningReminder.format(DateTimeFormatter.ofPattern("mm")),
                    onDoneButton = {
                        saveMorningReminder(it)
                        navController.navigateUp()
                    },
                    onNavigateBack = { navController.navigateUp() },
                    textTopBar = "Время напоминаний"
                )

                Hours.Evening -> SetReminderTime(
                    title = "Вечернее напоминание",
                    textHours = eveningReminder.format(DateTimeFormatter.ofPattern("HH")),
                    textMinutes = eveningReminder.format(DateTimeFormatter.ofPattern("mm")),
                    onDoneButton = {
                        saveEveningReminder(it)
                        navController.navigateUp()
                    },
                    onNavigateBack = { navController.navigateUp() },
                    textTopBar = "Время напоминаний"
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
                eveningTime = eveningReminder,
                morningTime = morningReminder,
                currentPage = currentPage,
                onChangeCurrentPage = { currentPage++ },
                onNameChanged = { signUpViewModel.onNameChanged(it) },
                onSurnameChanged = { signUpViewModel.onSurnameChanged(it) },
                onChangedPatronymic = { signUpViewModel.onChangedPatronymic(it) },
                onDateChanged = { signUpViewModel.onDateChanged(it) },
                onHeightChanged = { signUpViewModel.onHeightChanged(it) },
                onWeightChanged = { signUpViewModel.onWeightChanged(it) },
                onEndRegistration = {
                    if (eveningReminder.hour - morningReminder.hour < 8)
                        Toast.makeText(context, "Между утренним и вечерним напоминанием должно быть минимум 8 часов", Toast.LENGTH_LONG).show()
                    else
                        onEndRegistration(signUpUiState)
                },
                onClickSetReminder = { navController.navigate("${SetReminderTime.route}/${it}") }
            )
        }
    }
}