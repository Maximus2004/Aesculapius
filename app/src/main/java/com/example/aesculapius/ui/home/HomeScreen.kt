package com.example.aesculapius.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aesculapius.data.navigationItemContentList
import com.example.aesculapius.ui.theme.AesculapiusTheme
import com.example.aesculapius.ui.therapy.TherapyScreen
import com.example.aesculapius.ui.therapy.TopBarTherapy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val homeViewModel: HomeViewModel = viewModel()
    val homeUiState = homeViewModel.homeUiState.collectAsState().value
    Scaffold(
        topBar = { TopBarTherapy() },
        bottomBar = {
            BottomNavigationBar(
                currentTab = homeUiState.currentPage,
                onTabPressed = { homeViewModel.updateCurrentPage(it) },
                navigationItemContentList = navigationItemContentList
            )
        }) { contentPadding ->
        Column(
            modifier = modifier.padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding(),
                start = 11.dp,
                end = 11.dp
            )
        ) {
            when(homeUiState.currentPage) {
                PageType.Therapy -> TherapyScreen(modifier = Modifier)
                else -> TempScreen()
            }
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
    onTabPressed: ((PageType) -> Unit),
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
                alwaysShowLabel = false,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    AesculapiusTheme {
        HomeScreen()
    }
}