package com.example.aesculapius.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.aesculapius.ui.therapy.EditMedicineScreen
import com.example.aesculapius.ui.therapy.MedicineItem
import com.example.aesculapius.ui.therapy.NewMedicineScreen
import com.example.aesculapius.ui.therapy.TherapyScreen
import com.example.aesculapius.ui.therapy.TherapyViewModel
import java.time.LocalDate

@Composable
fun TherapyNavigation(
    isMorningMedicines: MutableState<Boolean>,
    turnOffBars: () -> Unit,
    modifier: Modifier = Modifier,
    turnOnBars: () -> Unit,
    onClickMedicine: (MedicineItem) -> Unit,
    medicine: MedicineItem?,
    therapyViewModel: TherapyViewModel,
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
                onClickMedicine = { onClickMedicine(it) },
                isMorningMedicines = isMorningMedicines
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
                    therapyViewModel.updateCurrentDate(therapyViewModel.getCurrentDate())
                },
                onClickUpdateMedicineItem = { medicineId, frequency, dose ->
                    therapyViewModel.updateMedicineItem(medicineId, frequency, dose)
                    navController.navigate(TherapyScreen.route) {
                        popUpTo(TherapyScreen.route) { inclusive = false }
                    }
                    therapyViewModel.updateCurrentDate(therapyViewModel.getCurrentDate())
                }
            )
        }
        composable(route = NewMedicineScreen.route) {
            NewMedicineScreen(
                onNavigateBack = { navController.navigateUp() },
                onClickFinishButton = { image, medicineType, name, undername, dose, frequency, startDate, endDate ->
                    therapyViewModel.addMedicineItem(image, medicineType, name, undername, dose, frequency, startDate, endDate, LocalDate.now())
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