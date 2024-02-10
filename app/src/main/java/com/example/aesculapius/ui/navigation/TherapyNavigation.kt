package com.example.aesculapius.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aesculapius.ui.profile.EditProfileScreen
import com.example.aesculapius.ui.tests.TestsScreen
import com.example.aesculapius.ui.tests.TestsViewModel
import com.example.aesculapius.ui.therapy.EditMedicineScreen
import com.example.aesculapius.ui.therapy.MedicineItem
import com.example.aesculapius.ui.therapy.NewMedicineScreen
import com.example.aesculapius.ui.therapy.TherapyScreen
import com.example.aesculapius.ui.therapy.TherapyViewModel
import java.time.LocalDate

@Composable
fun TherapyNavigation(
    turnOffBars: () -> Unit,
    modifier: Modifier = Modifier,
    turnOnBars: () -> Unit,
    onClickMedicine: (MedicineItem) -> Unit,
    medicine: MedicineItem?,
    therapyViewModel: TherapyViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val currentLoadingState = therapyViewModel.currentLoadingState
    val currentWeekDates = therapyViewModel.currentWeekDates.collectAsState().value
    val isWeek = therapyViewModel.isWeek.collectAsState().value

    NavHost(
        navController = navController,
        startDestination = TherapyScreen.route,
        modifier = modifier.fillMaxSize()
    ) {
        composable(route = TherapyScreen.route) {
            TherapyScreen(
                isWeek = isWeek,
                therapyViewModel = therapyViewModel,
                onCreateNewMedicine = { navController.navigate(NewMedicineScreen.route) },
                turnOnBars = { turnOnBars() },
                modifier = Modifier.padding(horizontal = 16.dp),
                currentLoadingState = currentLoadingState,
                currentWeekDates = currentWeekDates,
                getWeekDates = { therapyViewModel.getWeekDates(it) },
                currentDate = therapyViewModel.getCurrentDate(),
                updateCurrentDate = { therapyViewModel.updateCurrentDate(it) },
                isAfterCurrentDate = therapyViewModel.getCurrentDate().isAfter(LocalDate.now()),
                onClickChangeWeek = { therapyViewModel.changeIsWeek(it) },
                onClickMedicine = { onClickMedicine(it) }
            )
        }
        composable(route = EditMedicineScreen.route) {
            EditMedicineScreen(
                turnOffBars = turnOffBars,
                medicine = medicine!!,
                onNavigateBack = { navController.navigateUp() },
                onClickDeleteMedicine = { medicineId ->
                    therapyViewModel.deleteMedicineItem(medicineId)
                    navController.navigate(TherapyScreen.route) {
                        popUpTo(TherapyScreen.route) { inclusive = false }
                    }
                },
                onClickUpdateMedicineItem = { medicineId, frequency, dose ->
                    therapyViewModel.updateMedicineItem(medicineId, frequency, dose)
                    navController.navigate(TherapyScreen.route) {
                        popUpTo(TherapyScreen.route) { inclusive = false }
                    }
                }
            )
        }
        composable(route = NewMedicineScreen.route) {
            NewMedicineScreen(
                onNavigateBack = { navController.navigateUp() },
                onClickFinishButton = { image, medicineType, name, undername, dose, frequency, startDate, endDate ->
                    therapyViewModel.addMedicineItem(
                        image,
                        medicineType,
                        name,
                        undername,
                        dose,
                        frequency,
                        startDate,
                        endDate
                    )
                    navController.navigate(TherapyScreen.route) {
                        popUpTo(TherapyScreen.route) { inclusive = false }
                    }
                    therapyViewModel.updateCurrentDate(therapyViewModel.getCurrentDate())
                },
                turnOffBars = { turnOffBars() },
                currentDate = therapyViewModel.getCurrentDate(),
            )
        }
    }
}