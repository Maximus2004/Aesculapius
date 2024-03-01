package com.example.aesculapius

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.aesculapius.database.UserRemoteDataRepository
import com.example.aesculapius.notifications.MetricsAlarm
import com.example.aesculapius.ui.home.HomeScreen
import com.example.aesculapius.ui.navigation.SignUpNavigation
import com.example.aesculapius.ui.profile.ProfileViewModel
import com.example.aesculapius.ui.signup.SignUpUiState
import com.example.aesculapius.ui.theme.AesculapiusTheme
import com.example.aesculapius.worker.UserWorkerSchedule
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.AndroidEntryPoint
import java.time.ZoneId

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        setContent {
            AesculapiusTheme {
                val profileViewModel: ProfileViewModel = hiltViewModel()
                val userId = profileViewModel.userId.collectAsState().value
                val morningReminder = profileViewModel.morningReminder.collectAsState().value
                val eveningReminder = profileViewModel.eveningReminder.collectAsState().value
                val astTestDate = profileViewModel.ASTTestDate.collectAsState().value
                val recommendationTestDate = profileViewModel.recommendationTestDate.collectAsState().value
                val user: SignUpUiState = profileViewModel.user.collectAsState().value

                // если пользователь зарегистрирован, то мы должны попробовать сделать сохранение его данных
                if (userId != "")  {
                    val inputData = Data.Builder().putString("userId", userId).build()
                    val workRequest = OneTimeWorkRequestBuilder<UserWorkerSchedule>().setInputData(inputData).build()
                    WorkManager.getInstance(this).enqueue(workRequest)

                    HomeScreen(
                        morningReminder = morningReminder,
                        eveningReminder = eveningReminder,
                        saveMorningReminder = {
                            profileViewModel.saveMorningTime(it)
                            val morningTimeMillis = it.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                            val morningIntent = Intent(this, MetricsAlarm::class.java).apply {
                                putExtra("title", "Утреннее напоминание")
                                putExtra("message", "Не забудьте ввести метрики с пикфлоуметра!")
                            }
                            val morningPendingIntent = PendingIntent.getBroadcast(this, 0, morningIntent, PendingIntent.FLAG_IMMUTABLE)
                            profileViewModel.setMorningNotification(morningTimeMillis, morningPendingIntent)
                        },
                        saveEveningReminder = {
                            profileViewModel.saveEveningTime(it)
                            val eveningTimeMillis = it.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                            val eveningIntent = Intent(this, MetricsAlarm::class.java).apply {
                                putExtra("title", "Вечернее напоминание")
                                putExtra("message", "Не забудьте ввести метрики с пикфлоуметра!")
                            }
                            val eveningPendingIntent = PendingIntent.getBroadcast(this, 1, eveningIntent, PendingIntent.FLAG_IMMUTABLE)
                            profileViewModel.setEveningNotification(eveningTimeMillis, eveningPendingIntent)
                        },
                        recommendationTestDate = recommendationTestDate,
                        astTestDate = astTestDate,
                        saveAstDate = { profileViewModel.saveASTTestDate(it) },
                        saveRecommendationDate = { profileViewModel.saveRecommendationTestDate(it) },
                        user = user,
                        onSaveNewUser = { profileViewModel.updateUserProfile(it, userId) }
                    )
                }
                else SignUpNavigation(
                    morningReminder = morningReminder,
                    eveningReminder = eveningReminder,
                    saveMorningReminder = { profileViewModel.saveMorningTime(it) },
                    saveEveningReminder = { profileViewModel.saveEveningTime(it) },
                    onEndRegistration = {
                        profileViewModel.changeUserAtFirst(UserRemoteDataRepository.getUserId(), it)

                        val morningTimeMillis = morningReminder.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        val morningIntent = Intent(this, MetricsAlarm::class.java).apply {
                            putExtra("title", "Утреннее напоминание")
                            putExtra("message", "Не забудьте ввести метрики с пикфлоуметра!")
                        }
                        val morningPendingIntent = PendingIntent.getBroadcast(this, 0, morningIntent, PendingIntent.FLAG_IMMUTABLE)
                        profileViewModel.setMorningNotification(morningTimeMillis, morningPendingIntent)

                        val eveningTimeMillis = eveningReminder.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        val eveningIntent = Intent(this, MetricsAlarm::class.java).apply {
                            putExtra("title", "Вечернее напоминание")
                            putExtra("message", "Не забудьте ввести метрики с пикфлоуметра!")
                        }
                        val eveningPendingIntent = PendingIntent.getBroadcast(this, 1, eveningIntent, PendingIntent.FLAG_IMMUTABLE)
                        profileViewModel.setEveningNotification(eveningTimeMillis, eveningPendingIntent)
                    }
                )
            }
        }
    }
}