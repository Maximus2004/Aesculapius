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
import com.example.aesculapius.ui.signup.SignUpEvent
import com.example.aesculapius.ui.signup.SignUpScreen
import com.example.aesculapius.ui.signup.SignUpUiState
import com.example.aesculapius.ui.signup.SignUpViewModel
import java.time.format.DateTimeFormatter

@Composable
fun SignUpNavigation(
    onEndRegistration: (SignUpUiState) -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val signUpViewModel: SignUpViewModel = viewModel()
    val signUpUiState by signUpViewModel.uiStateSingUp.collectAsState()
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
                    textHours = signUpUiState.morningReminder.format(DateTimeFormatter.ofPattern("HH")),
                    textMinutes = signUpUiState.morningReminder.format(DateTimeFormatter.ofPattern("mm")),
                    onDoneButton = {
                        signUpViewModel.onEvent(SignUpEvent.OnMorningReminderChanged(it))
                        navController.navigateUp()
                    },
                    onNavigateBack = { navController.navigateUp() },
                    textTopBar = "Время напоминаний"
                )

                Hours.Evening -> SetReminderTime(
                    title = "Вечернее напоминание",
                    textHours = signUpUiState.eveningReminder.format(DateTimeFormatter.ofPattern("HH")),
                    textMinutes = signUpUiState.eveningReminder.format(DateTimeFormatter.ofPattern("mm")),
                    onDoneButton = {
                        signUpViewModel.onEvent(SignUpEvent.OnEveningReminderChanged(it))
                        navController.navigateUp()
                    },
                    onNavigateBack = { navController.navigateUp() },
                    textTopBar = "Время напоминаний"
                )
            }
        }
        composable(route = SignUpScreen.route) {
            SignUpScreen(
                userUiState = signUpUiState,
                currentPage = currentPage,
                onChangeCurrentPage = { currentPage++ },
                onEvent = signUpViewModel::onEvent,
                onEndRegistration = {
                    if (signUpUiState.eveningReminder.hour - signUpUiState.morningReminder.hour < 8)
                        Toast.makeText(context, "Между утренним и вечерним напоминанием должно быть минимум 8 часов", Toast.LENGTH_LONG).show()
                    else
                        onEndRegistration(signUpUiState)
                },
                onClickSetReminder = { navController.navigate("${SetReminderTime.route}/${it}") }
            )
        }
    }
}