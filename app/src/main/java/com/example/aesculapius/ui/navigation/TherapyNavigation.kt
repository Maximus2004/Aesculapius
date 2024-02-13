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
import com.example.aesculapius.ui.therapy.MedicineCard
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
    onClickMedicine: (MedicineCard) -> Unit,
    medicine: MedicineCard?,
    therapyViewModel: TherapyViewModel,
    navController: NavHostController
) {
    val currentLoadingState = therapyViewModel.currentLoadingState.collectAsState().value
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
            )
        }
        composable(route = EditMedicineScreen.route) {
            EditMedicineScreen(
                turnOffBars = turnOffBars,
                medicine = medicine!!,
                onNavigateBack = { navController.navigateUp() },
                onClickDeleteMedicine = {
                    therapyViewModel.deleteMedicineItem(medicine.id)
                    navController.navigate(TherapyScreen.route) {
                        popUpTo(TherapyScreen.route) { inclusive = false }
                    }
                },
                onClickUpdateMedicineItem = { frequency, dose ->
                    therapyViewModel.updateMedicineItem(medicine.id, frequency, dose, medicine.medicineType, medicine.startDate, medicine.endDate)
                    navController.navigate(TherapyScreen.route) {
                        popUpTo(TherapyScreen.route) { inclusive = false }
                    }
                }
            )
        }
        composable(route = NewMedicineScreen.route) {
            NewMedicineScreen(
                onNavigateBack = { navController.navigateUp() },
                onClickFinishButton = { medicineType, name, undername, dose, frequency, startDate, endDate ->
                    therapyViewModel.addMedicineItem(medicineType, name, undername, dose, frequency, startDate, endDate)
                    navController.navigate(TherapyScreen.route) {
                        popUpTo(TherapyScreen.route) { inclusive = false }
                    }
                },
                turnOffBars = { turnOffBars() },
            )
        }
    }
}