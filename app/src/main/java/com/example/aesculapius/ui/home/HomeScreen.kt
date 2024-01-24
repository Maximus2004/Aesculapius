package com.example.aesculapius.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aesculapius.data.navigationItemContentList
import com.example.aesculapius.ui.navigation.NavigationDestination
import com.example.aesculapius.ui.navigation.ProfileNavigation
import com.example.aesculapius.ui.navigation.TestsNavigation
import com.example.aesculapius.ui.navigation.TherapyNavigation
import com.example.aesculapius.ui.profile.ProfileScreen
import com.example.aesculapius.ui.statistics.StatisticsScreen
import com.example.aesculapius.ui.tests.TestsScreen
import com.example.aesculapius.ui.theme.AesculapiusTheme
import com.example.aesculapius.ui.therapy.TherapyScreen
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

object HomeScreen : NavigationDestination {
    override val route = "HomeScreen"
}

@Composable
fun HomeScreen(
    saveASTDate: (LocalDate) -> Unit,
    saveRecommendationDate:  (LocalDate) -> Unit,
    recommendationTestDate: String,
    ASTTestDate: String,
    morningReminder: LocalDateTime,
    eveningReminder: LocalDateTime,
    saveMorningReminder: (LocalDateTime) -> Unit,
    saveEveningReminder: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val homeViewModel: HomeViewModel = viewModel()
    val homeUiState = homeViewModel.homeUiState.collectAsState().value
    var isBarsDisplayed by remember { mutableStateOf(true) }
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
                    turnOnBars = { isBarsDisplayed = true })

                PageType.Profile -> ProfileNavigation(
                    morningReminder = morningReminder,
                    eveningReminder = eveningReminder,
                    saveMorningReminder = { saveMorningReminder(it) },
                    saveEveningReminder = { saveEveningReminder(it) },
                    turnOffBars = { isBarsDisplayed = false },
                    turnOnBars = { isBarsDisplayed = true }
                )

                PageType.Statistics -> StatisticsScreen(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(color = Color.White)
                )

                PageType.Tests -> TestsNavigation(
                    saveMorningReminder = { saveMorningReminder(it) },
                    saveEveningReminder = { saveEveningReminder(it) },
                    saveASTDate = { saveASTDate(it) },
                    saveRecommendationDate = { saveRecommendationDate(it) },
                    ASTTestDate = ASTTestDate,
                    recommendationTestDate = recommendationTestDate,
                    morningReminder = morningReminder,
                    eveningReminder = eveningReminder,
                    turnOffBars = { isBarsDisplayed = false },
                    turnOnBars = { isBarsDisplayed = true })
            }
        }
    }
}

@Composable
fun TopBar(modifier: Modifier = Modifier, screenName: String, isHelpButton: Boolean) {
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
                onClick = { /*TODO*/ },
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
fun TempScreen() {
    Text(text = "Находится в разработке")
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