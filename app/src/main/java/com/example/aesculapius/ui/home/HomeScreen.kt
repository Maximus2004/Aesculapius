package com.example.aesculapius.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aesculapius.data.navigationItemContentList
import com.example.aesculapius.ui.navigation.NavigationDestination
import com.example.aesculapius.ui.navigation.ProfileNavigation
import com.example.aesculapius.ui.navigation.TestsNavigation
import com.example.aesculapius.ui.navigation.TherapyNavigation
import com.example.aesculapius.ui.signup.SignUpUiState
import com.example.aesculapius.ui.statistics.StatisticsScreen
import java.time.LocalDate
import java.time.LocalDateTime
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.TextButton
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.aesculapius.R
import com.example.aesculapius.ui.statistics.StatisticsViewModel
import com.example.aesculapius.ui.tests.TestsViewModel
import com.example.aesculapius.ui.theme.AesculapiusTheme
import com.example.aesculapius.ui.therapy.EditMedicineScreen
import com.example.aesculapius.ui.therapy.MedicineItem
import com.example.aesculapius.ui.therapy.TherapyViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    saveAstDate: (LocalDate) -> Unit,
    saveRecommendationDate: (LocalDate) -> Unit,
    recommendationTestDate: String,
    astTestDate: String,
    morningReminder: LocalDateTime,
    eveningReminder: LocalDateTime,
    saveMorningReminder: (LocalDateTime) -> Unit,
    saveEveningReminder: (LocalDateTime) -> Unit,
    user: SignUpUiState,
    onSaveNewUser: (SignUpUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    val homeViewModel: HomeViewModel = viewModel()
    val therapyViewModel: TherapyViewModel = hiltViewModel()
    val testsViewModel: TestsViewModel = hiltViewModel()
    val statisticsViewModel: StatisticsViewModel = hiltViewModel()

    val homeUiState = homeViewModel.homeUiState.collectAsState().value
    var isBarsDisplayed by remember { mutableStateOf(true) }
    val currentMedicineItem: MutableState<MedicineItem?> = remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val navController = rememberNavController()
    val isMorningMedicines = remember { mutableStateOf(true) }

    ModalBottomSheetLayout(
        sheetContent = {
            if (currentMedicineItem.value != null)
                EditMedicineSheet(
                    medicine = currentMedicineItem.value!!,
                    navigateToEditMedicineScreen = {
                        scope.launch { sheetState.hide() }
                        navController.navigate(EditMedicineScreen.route)
                    },
                    skipMedicine = {
                        scope.launch { sheetState.hide() }
                        therapyViewModel.skipMedicine(it, isMorningMedicines.value)
                        therapyViewModel.updateCurrentDate(therapyViewModel.getCurrentDate())
                    },
                    acceptMedicine = {
                        scope.launch { sheetState.hide() }
                        therapyViewModel.acceptMedicine(it, isMorningMedicines.value)
                        therapyViewModel.updateCurrentDate(therapyViewModel.getCurrentDate())
                    }
                )
        },
        sheetShape = RoundedCornerShape(
            topEnd = 8.dp,
            topStart = 8.dp,
            bottomEnd = 0.dp,
            bottomStart = 0.dp
        ),
        sheetState = sheetState
    ) {
        Scaffold(
            topBar = {
                if (isBarsDisplayed) TopBar(
                    screenName = homeUiState.currentPageName,
                    isHelpButton = homeUiState.isHelpButton
                )
            },
            bottomBar = {
                if (isBarsDisplayed) BottomNavigationBar(
                    currentTab = homeUiState.currentPage,
                    onTabPressed = { pageType, pageName, isHelpButton ->
                        homeViewModel.updateCurrentPage(pageType, pageName, isHelpButton)
                    },
                    navigationItemContentList = navigationItemContentList
                )
            }) { contentPadding ->
            Column(
                modifier = modifier.padding(
                    top = contentPadding.calculateTopPadding(),
                    bottom = contentPadding.calculateBottomPadding()
                )
            ) {
                when (homeUiState.currentPage) {
                    PageType.Therapy -> TherapyNavigation(
                        turnOffBars = { isBarsDisplayed = false },
                        turnOnBars = { isBarsDisplayed = true },
                        onClickMedicine = {
                            currentMedicineItem.value = it
                            scope.launch { sheetState.show() }
                        },
                        medicine = currentMedicineItem.value,
                        navController = navController,
                        isMorningMedicines = isMorningMedicines,
                        therapyViewModel = therapyViewModel
                    )

                    PageType.Profile -> ProfileNavigation(
                        morningReminder = morningReminder,
                        eveningReminder = eveningReminder,
                        saveMorningReminder = { saveMorningReminder(it) },
                        saveEveningReminder = { saveEveningReminder(it) },
                        turnOffBars = { isBarsDisplayed = false },
                        turnOnBars = { isBarsDisplayed = true },
                        user = user,
                        onSaveNewUser = onSaveNewUser,
                        navController = navController,
                    )

                    PageType.Statistics -> StatisticsScreen(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(color = Color.White),
                        statisticsViewModel = statisticsViewModel
                    )

                    PageType.Tests -> TestsNavigation(
                        saveMorningReminder = { saveMorningReminder(it) },
                        saveEveningReminder = { saveEveningReminder(it) },
                        saveASTDate = { saveAstDate(it) },
                        saveRecommendationDate = { saveRecommendationDate(it) },
                        ASTTestDate = astTestDate,
                        recommendationTestDate = recommendationTestDate,
                        morningReminder = morningReminder,
                        eveningReminder = eveningReminder,
                        turnOffBars = { isBarsDisplayed = false },
                        turnOnBars = { isBarsDisplayed = true },
                        navController = navController,
                        testsViewModel = testsViewModel
                    )
                }
            }
        }
    }
}

/** [TopBar] для главного экрана без навигации */
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    screenName: String,
    isHelpButton: Boolean,
    onClickHelpButton: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 26.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = screenName,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 24.dp)
        )
        Spacer(Modifier.weight(1f))
        if (isHelpButton)
            IconButton(
                onClick = { onClickHelpButton() },
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = Color(0xFF49454F)
                )
            }
    }
}

@Composable
fun BottomNavigationBar(
    currentTab: PageType,
    onTabPressed: ((PageType, String, Boolean) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .height(63.dp),
        containerColor = MaterialTheme.colorScheme.onBackground,
        tonalElevation = 10.dp
    ) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = currentTab == navItem.pageType,
                onClick = {
                    onTabPressed(
                        navItem.pageType,
                        navItem.pageName,
                        navItem.isHelpButton
                    )
                },
                icon = {
                    Image(
                        painterResource(id = navItem.icon),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp)
                    )
                },
                alwaysShowLabel = false,
            )
        }
    }
}

@Composable
fun EditMedicineSheet(
    acceptMedicine: (Int) -> Unit,
    skipMedicine: (Int) -> Unit,
    navigateToEditMedicineScreen: () -> Unit,
    medicine: MedicineItem,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(24.dp)) {
        Row {
            Column {
                Text(
                    text = medicine.name,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Black
                )
                Text(text = medicine.undername, style = MaterialTheme.typography.bodySmall)
                Text(text = medicine.dose, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = navigateToEditMedicineScreen) {
                Icon(painterResource(id = R.drawable.edit_icon), contentDescription = null)
            }
        }
        Row(modifier = Modifier.padding(top = 16.dp)) {
            if ("вечером" in medicine.frequency)
                Icon(
                    painter = painterResource(id = R.drawable.moon_icon),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp, bottom = 12.dp),
                    tint = Color.Black
                )
            else
                Icon(
                    painter = painterResource(id = R.drawable.sun_icon),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp, bottom = 12.dp),
                    tint = Color.Black
                )
            Text(
                text = medicine.frequency,
                style = MaterialTheme.typography.labelSmall,
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.secondary
        )
        Row(modifier = Modifier.align(Alignment.End)) {
            TextButton(
                onClick = { skipMedicine(medicine.id) },
                modifier = Modifier
                    .width(106.dp)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = "Пропустить",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Button(
                onClick = { acceptMedicine(medicine.id) },
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ),
                modifier = Modifier.width(106.dp)
            ) {
                Text(
                    text = "Принять",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}