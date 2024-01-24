package com.example.aesculapius.ui.profile

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import com.example.aesculapius.database.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import java.util.prefs.Preferences
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.aesculapius.database.Converters
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@HiltViewModel
class ProfileViewModel @Inject constructor(private val prefRepository: UserPreferencesRepository): ViewModel() {
    val isUserRegistered: StateFlow<Boolean> = prefRepository.isUserRegistered
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val morningReminder: StateFlow<LocalDateTime> = prefRepository.morningReminder
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LocalDateTime.now()
        )

    val eveningReminder: StateFlow<LocalDateTime> = prefRepository.eveningReminder
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LocalDateTime.now()
        )

    val ASTTestDate: StateFlow<String> = prefRepository.astTest
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    val recommendationTest: StateFlow<String> = prefRepository.recommendationTest
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    fun saveASTTestDate(astTestDate: LocalDate) {
        viewModelScope.launch {
            prefRepository.saveASTTestDate(Converters.dateToStringWithFormat(astTestDate))
        }
    }

    fun saveRecommendationTestDate(recommendationTestDate: LocalDate) {
        viewModelScope.launch {
            prefRepository.saveRecommendationTest(Converters.dateToStringWithFormat(recommendationTestDate))
        }
    }

    fun saveMorningTime(morningTime: LocalDateTime) {
        viewModelScope.launch {
            prefRepository.saveUserMorningReminder(Converters.timeToString(morningTime))
        }
    }

    fun saveEveningTime(eveningTime: LocalDateTime) {
        viewModelScope.launch {
            prefRepository.saveUserEveningReminder(Converters.timeToString(eveningTime))
        }
    }

    fun changeUser(isUserRegistered: Boolean) {
        viewModelScope.launch {
            prefRepository.saveUserPreferences(isUserRegistered)
        }
    }
}