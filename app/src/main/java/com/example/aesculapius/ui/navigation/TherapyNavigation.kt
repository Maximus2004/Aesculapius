package com.example.aesculapius.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aesculapius.ui.tests.TestsScreen
import com.example.aesculapius.ui.tests.TestsViewModel
import com.example.aesculapius.ui.therapy.NewMedicineScreen
import com.example.aesculapius.ui.therapy.TherapyScreen
import com.example.aesculapius.ui.therapy.TherapyViewModel
import java.time.LocalDate

@Composable
fun TherapyNavigation(
    turnOffBars: () -> Unit,
    modifier: Modifier = Modifier,
    turnOnBars: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val therapyViewModel: TherapyViewModel = viewModel()
    val currentLoadingState = therapyViewModel.currentLoadingState
    val currentWeekDates = therapyViewModel.currentWeekDates.collectAsState().value

    NavHost(
        navController = navController,
        startDestination = TherapyScreen.route,
        modifier = modifier.fillMaxSize()
    ) {
        composable(route = TherapyScreen.route) {
            TherapyScreen(
                onCreateNewMedicine = {
                    navController.navigate(NewMedicineScreen.route)
                    turnOffBars()
                },
                modifier = Modifier.padding(horizontal = 16.dp),
                currentLoadingState = currentLoadingState,
                currentWeekDates = currentWeekDates,
                getWeekDates = { therapyViewModel.getWeekDates(it) },
                currentDate = therapyViewModel.getCurrentDate(),
                updateCurrentDate = { therapyViewModel.updateCurrentDate(it) },
                getActiveAmount = { therapyViewModel.getAmountActive(it) },
                isAfterCurrentDate = therapyViewModel.getCurrentDate().isAfter(LocalDate.now())
            )
        }
        composable(route = NewMedicineScreen.route) {
            NewMedicineScreen(
                onNavigateBack = {
                    turnOnBars()
                    navController.navigateUp()
                },
                onClickFinishButton = {
                    therapyViewModel.addMedicineItem(it)
                    navController.popBackStack()
                    navController.navigate(TherapyScreen.route)
                    therapyViewModel.updateCurrentDate(LocalDate.now())
                    turnOnBars()
                },
                currentDate = therapyViewModel.getCurrentDate(),
            )
        }
    }
}