package com.example.aesculapius.ui.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.aesculapius.R
import com.example.aesculapius.data.navigationItemContentList
import com.example.aesculapius.data.topBarHomeScreen
import com.example.aesculapius.ui.navigation.profileNavGraph
import com.example.aesculapius.ui.navigation.statisticsNavGraph
import com.example.aesculapius.ui.navigation.testsNavGraph
import com.example.aesculapius.ui.navigation.therapyNavGraph
import com.example.aesculapius.ui.profile.ProfileEvent
import com.example.aesculapius.ui.signup.SignUpUiState
import com.example.aesculapius.ui.statistics.StatisticsViewModel
import com.example.aesculapius.ui.tests.TestsViewModel
import com.example.aesculapius.ui.theme.tertiaryContainer
import com.example.aesculapius.ui.therapy.EditMedicineScreen
import com.example.aesculapius.ui.therapy.MedicineCard
import com.example.aesculapius.ui.therapy.TherapyEvent
import com.example.aesculapius.ui.therapy.TherapyScreen
import com.example.aesculapius.ui.therapy.TherapyViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    onProfileEvent: (ProfileEvent) -> Unit,
    userUiState: SignUpUiState,
) {
    val therapyViewModel: TherapyViewModel = hiltViewModel()
    val testsViewModel: TestsViewModel = hiltViewModel()
    val statisticsViewModel: StatisticsViewModel = hiltViewModel()

    var isBarsDisplayed by remember { mutableStateOf(true) }
    val currentMedicineItem: MutableState<MedicineCard?> = remember { mutableStateOf(null) }

    // используется при нажатии на препарат
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val navController = rememberAnimatedNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute: String = navBackStackEntry?.destination?.route ?: TherapyScreen.route

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
                        therapyViewModel.onTherapyEvent(TherapyEvent.OnSkipMedicine(it))
                    },
                    acceptMedicine = {
                        scope.launch { sheetState.hide() }
                        therapyViewModel.onTherapyEvent(TherapyEvent.OnAcceptMedicine(it))
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
                    screenName = stringResource(
                        id = topBarHomeScreen[currentRoute]?.first ?: R.string.therapy_name
                    ),
                    isHelpButton = topBarHomeScreen[currentRoute]?.second ?: false
                )
            },
            bottomBar = {
                if (isBarsDisplayed) BottomNavigationBar(
                    currentTab = currentRoute,
                    onTabPressed = { pageType ->
                        navController.navigate(pageType)
                    },
                    navigationItemContentList = navigationItemContentList
                )
            }) { contentPadding ->
            NavHost(
                navController = navController,
                startDestination = TherapyScreen.route,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues = contentPadding),
                builder = {
                    therapyNavGraph(
                        onClickMedicine = {
                            currentMedicineItem.value = it
                            scope.launch { sheetState.show() }
                        },
                        medicine = currentMedicineItem.value,
                        navController = navController,
                        therapyViewModel = therapyViewModel,
                        turnOnBars = { isBarsDisplayed = true },
                        turnOffBars = { isBarsDisplayed = false }
                    )

                    profileNavGraph(
                        turnOffBars = { isBarsDisplayed = false },
                        turnOnBars = { isBarsDisplayed = true },
                        userUiState = userUiState,
                        navController = navController,
                        onProfileEvent = onProfileEvent
                    )

                    statisticsNavGraph(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(color = tertiaryContainer),
                        statisticsViewModel = statisticsViewModel,
                        userUiState = userUiState
                    )

                    testsNavGraph(
                        userUiState = userUiState,
                        turnOffBars = { isBarsDisplayed = false },
                        turnOnBars = { isBarsDisplayed = true },
                        navController = navController,
                        testsViewModel = testsViewModel,
                        onProfileEvent = onProfileEvent
                    )
                }
            )
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
                    tint = MaterialTheme.colorScheme.onTertiary
                )
            }
    }
}

@Composable
fun BottomNavigationBar(
    currentTab: String,
    onTabPressed: ((String) -> Unit),
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
                onClick = { onTabPressed(navItem.pageType) },
                icon = {
                    Image(
                        painterResource(id = navItem.icon),
                        contentDescription = null,
                        modifier = Modifier.size(25.dp)
                    )
                },
                alwaysShowLabel = false
            )
        }
    }
}

@Composable
fun EditMedicineSheet(
    acceptMedicine: (Int) -> Unit,
    skipMedicine: (Int) -> Unit,
    navigateToEditMedicineScreen: () -> Unit,
    medicine: MedicineCard,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(24.dp)) {
        Row {
            Column {
                Text(
                    text = medicine.name,
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = medicine.undername,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primaryContainer
                )
                Text(
                    text = medicine.dose,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primaryContainer
                )
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
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            else
                Icon(
                    painter = painterResource(id = R.drawable.sun_icon),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp, bottom = 12.dp),
                    tint = MaterialTheme.colorScheme.onSecondary
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
                onClick = { skipMedicine(medicine.doseId) },
                modifier = Modifier
                    .width(106.dp)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.skip),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Button(
                onClick = { acceptMedicine(medicine.doseId) },
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                modifier = Modifier.width(106.dp)
            ) {
                Text(
                    text = stringResource(R.string.accept),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}