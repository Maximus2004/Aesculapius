package com.example.aesculapius.ui.profile

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.aesculapius.database.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.aesculapius.database.Converters
import com.example.aesculapius.database.UserRemoteDataRepository
import com.example.aesculapius.notifications.MetricsAlarm
import com.example.aesculapius.ui.signup.SignUpUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val prefRepository: UserPreferencesRepository,
    private val userRemoteDataRepository: UserRemoteDataRepository,
    private val application: Application
) : ViewModel() {
    private val morningAlarmManager = application.applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager
    private val eveningAlarmManager = application.applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager

    val userUiState: StateFlow<SignUpUiState> = prefRepository.user
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SignUpUiState()
        )

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnSaveAstTestDate -> {
                viewModelScope.launch {
                    prefRepository.saveAstTestDate(Converters.dateToStringWithFormat(event.astTestDate))
                }
            }
            is ProfileEvent.OnSaveRecommendationTestDate -> {
                viewModelScope.launch {
                    prefRepository.saveRecommendationTest(
                        Converters.dateToStringWithFormat(event.recommendationTestDate)
                    )
                }
            }
            is ProfileEvent.OnUpdateUserProfile -> {
                viewModelScope.launch {
                    prefRepository.saveUserData(event.signUpUiState)
                    userRemoteDataRepository.updateUserProfile(event.signUpUiState)
                }
            }
            is ProfileEvent.OnSaveEveningTime -> {
                viewModelScope.launch {
                    prefRepository.saveUserEveningReminder(Converters.timeToString(event.eveningTime))
                    setEveningNotification(event.eveningTime)
                }
            }
            is ProfileEvent.OnSaveMorningTime -> {
                viewModelScope.launch {
                    prefRepository.saveUserEveningReminder(Converters.timeToString(event.morningTime))
                    setMorningNotification(event.morningTime)
                }
            }
            is ProfileEvent.OnSaveNewUser -> {
                viewModelScope.launch {
                    prefRepository.saveUserData(event.signUpUiState)
                    userRemoteDataRepository.addUserAtFirst(event.signUpUiState)
                    initMorningEveningNotifications(event.signUpUiState.morningReminder, event.signUpUiState.eveningReminder)
                }
            }
        }
    }

    private fun setMorningNotification(morningTimeMillis: Long, morningPendingIntent: PendingIntent) {
        morningAlarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            morningTimeMillis,
            AlarmManager.INTERVAL_DAY,
            morningPendingIntent
        )
    }

    private fun setEveningNotification(eveningTimeMillis: Long, eveningPendingIntent: PendingIntent) {
        eveningAlarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            eveningTimeMillis,
            AlarmManager.INTERVAL_DAY,
            eveningPendingIntent
        )
    }

    private fun setMorningNotification(morningTime: LocalDateTime) {
        val morningTimeMillis = morningTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val morningIntent = Intent(application.applicationContext, MetricsAlarm::class.java).apply {
            putExtra("title", "Утреннее напоминание")
            putExtra("message", "Не забудьте ввести метрики с пикфлоуметра!")
        }
        val morningPendingIntent = PendingIntent.getBroadcast(
            application.applicationContext,
            0,
            morningIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        setMorningNotification(
            morningTimeMillis,
            morningPendingIntent
        )
    }

    fun setEveningNotification(eveningTime: LocalDateTime) {
        val eveningTimeMillis = eveningTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val eveningIntent = Intent(application.applicationContext, MetricsAlarm::class.java).apply {
            putExtra("title", "Вечернее напоминание")
            putExtra("message", "Не забудьте ввести метрики с пикфлоуметра!")
        }
        val eveningPendingIntent = PendingIntent.getBroadcast(
            application.applicationContext,
            1,
            eveningIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        setEveningNotification(
            eveningTimeMillis,
            eveningPendingIntent
        )
    }

    fun initMorningEveningNotifications(morningReminder: LocalDateTime, eveningReminder: LocalDateTime) {
        val morningTimeMillis = morningReminder.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val morningIntent = Intent(application.applicationContext, MetricsAlarm::class.java).apply {
            putExtra("title", "Утреннее напоминание")
            putExtra("message", "Не забудьте ввести метрики с пикфлоуметра!")
        }
        val morningPendingIntent = PendingIntent.getBroadcast(
            application.applicationContext,
            0,
            morningIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        setMorningNotification(
            morningTimeMillis,
            morningPendingIntent
        )

        val eveningTimeMillis = eveningReminder.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val eveningIntent = Intent(application.applicationContext, MetricsAlarm::class.java).apply {
            putExtra("title", "Вечернее напоминание")
            putExtra("message", "Не забудьте ввести метрики с пикфлоуметра!")
        }
        val eveningPendingIntent = PendingIntent.getBroadcast(
            application.applicationContext,
            1,
            eveningIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        setEveningNotification(
            eveningTimeMillis,
            eveningPendingIntent
        )
    }
}