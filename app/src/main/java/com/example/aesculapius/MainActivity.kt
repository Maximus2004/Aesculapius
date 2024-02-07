package com.example.aesculapius

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.aesculapius.database.UserRemoteDataRepository
import com.example.aesculapius.ui.home.HomeScreen
import com.example.aesculapius.ui.navigation.MainNavigation
import com.example.aesculapius.ui.profile.ProfileViewModel
import com.example.aesculapius.ui.theme.AesculapiusTheme
import com.example.aesculapius.worker.UserWorkerSchedule
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
//        Log.i("TAGTAG", "message3")
        setContent {
            AesculapiusTheme {
                val profileViewModel: ProfileViewModel = hiltViewModel()
                val userId = profileViewModel.userId.collectAsState().value
                val morningReminder = profileViewModel.morningReminder.collectAsState().value
                val eveningReminder = profileViewModel.eveningReminder.collectAsState().value
                val ASTTestDate = profileViewModel.ASTTestDate.collectAsState().value
                val recommendationTest = profileViewModel.recommendationTest.collectAsState().value

                // если пользователь зарегистрирован, то мы должны попробовать сделать сохранение его данных
                if (userId != "")  {
                    val inputData = Data.Builder().putString("userId", userId).build()
                    val workRequest = OneTimeWorkRequestBuilder<UserWorkerSchedule>().setInputData(inputData).build()
                    WorkManager.getInstance(this).enqueue(workRequest)

                    HomeScreen(
                        morningReminder = morningReminder,
                        eveningReminder = eveningReminder,
                        saveMorningReminder = { profileViewModel.saveMorningTime(it) },
                        saveEveningReminder = { profileViewModel.saveEveningTime(it) },
                        recommendationTestDate = recommendationTest,
                        ASTTestDate = ASTTestDate,
                        saveASTDate = { profileViewModel.saveASTTestDate(it) },
                        saveRecommendationDate = { profileViewModel.saveRecommendationTestDate(it) }
                    )
                }
                else MainNavigation(
                    morningReminder = morningReminder,
                    eveningReminder = eveningReminder,
                    saveMorningReminder = { profileViewModel.saveMorningTime(it) },
                    saveEveningReminder = { profileViewModel.saveEveningTime(it) },
                    onEndRegistration = { profileViewModel.changeUserAtFirst(UserRemoteDataRepository.getUserId(), it) },
                    recommendationTestDate = recommendationTest,
                    ASTTestDate = ASTTestDate,
                    saveASTDate = { profileViewModel.saveASTTestDate(it) },
                    saveRecommendationDate = { profileViewModel.saveRecommendationTestDate(it) }
                )
            }
        }
    }
}