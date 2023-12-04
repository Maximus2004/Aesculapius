package com.example.aesculapius.ui.home

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel() : ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState

    fun updateCurrentPage(pageType: PageType, currentPageName: String, isHelpButton: Boolean) {
        _homeUiState.update {
            HomeUiState(
                currentPage = pageType,
                currentPageName = currentPageName,
                isHelpButton = isHelpButton
            )
        }
    }
}