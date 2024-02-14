package com.example.aesculapius.ui.profile

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import com.example.aesculapius.database.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.aesculapius.database.AesculapiusRepository
import com.example.aesculapius.database.Converters
import com.example.aesculapius.database.UserRemoteDataRepository
import com.example.aesculapius.notifications.MyAlarm
import com.example.aesculapius.ui.signup.SignUpUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val prefRepository: UserPreferencesRepository,
    private val userRemoteDataRepository: UserRemoteDataRepository,
    application: Application
) : ViewModel() {
    val morningAlarmManager = application.applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager
    val eveningAlarmManager = application.applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager

    val user: StateFlow<SignUpUiState> = prefRepository.user
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SignUpUiState()
        )

    val userId: StateFlow<String> = prefRepository.userId
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
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

    val recommendationTestDate: StateFlow<String> = prefRepository.recommendationTest
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    fun setMorningNotification(morningTimeMillis: Long, morningPendingIntent: PendingIntent) {
        morningAlarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            morningTimeMillis,
            AlarmManager.INTERVAL_DAY,
            morningPendingIntent
        )
    }

    fun setEveningNotification(eveningTimeMillis: Long, eveningPendingIntent: PendingIntent) {
        eveningAlarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            eveningTimeMillis,
            AlarmManager.INTERVAL_DAY,
            eveningPendingIntent
        )
    }

    fun saveASTTestDate(astTestDate: LocalDate) {
        viewModelScope.launch {
            prefRepository.saveAstTestDate(Converters.dateToStringWithFormat(astTestDate))
        }
    }

    fun saveRecommendationTestDate(recommendationTestDate: LocalDate) {
        viewModelScope.launch {
            prefRepository.saveRecommendationTest(
                Converters.dateToStringWithFormat(
                    recommendationTestDate
                )
            )
        }
    }

    fun updateUserProfile(user: SignUpUiState, userId: String) {
        viewModelScope.launch {
            prefRepository.saveUserData(user)
            userRemoteDataRepository.updateUserProfile(user, userId)
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

    fun changeUserAtFirst(userId: String, signUpUiState: SignUpUiState) {
        viewModelScope.launch {
            prefRepository.saveUserPreferences(userId)
            prefRepository.saveUserData(signUpUiState)
            userRemoteDataRepository.addUserAtFirst(userId, signUpUiState)
        }
    }
}