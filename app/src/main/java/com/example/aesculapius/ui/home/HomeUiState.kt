package com.example.aesculapius.ui.home

data class HomeUiState(
    val currentPage: PageType = PageType.Therapy,
    val currentPageName: String = "Базисная терапия",
    val isHelpButton: Boolean = false
)