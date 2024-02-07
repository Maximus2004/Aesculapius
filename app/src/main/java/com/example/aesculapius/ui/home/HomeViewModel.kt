package com.example.aesculapius.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.aesculapius.database.USERS_COLLECTION_REF
import com.example.aesculapius.database.UserRemoteDataRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class HomeViewModel : ViewModel() {
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