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
import java.time.LocalTime

@HiltViewModel
class ProfileViewModel @Inject constructor(private val prefRepository: UserPreferencesRepository): ViewModel() {
    val isUserRegistered: StateFlow<Boolean> = prefRepository.isUserRegistered
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val morningReminder: StateFlow<LocalTime> = prefRepository.morningReminder
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LocalTime.now()
        )

    val eveningReminder: StateFlow<LocalTime> = prefRepository.eveningReminder
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LocalTime.now()
        )

    fun saveMorningTime(morningTime: LocalTime) {
        viewModelScope.launch {
            prefRepository.saveUserMorningReminder(Converters.timeToString(morningTime))
        }
    }

    fun saveEveningTime(eveningTime: LocalTime) {
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