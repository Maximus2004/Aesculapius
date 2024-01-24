package com.example.aesculapius

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.aesculapius.ui.home.HomeScreen
import com.example.aesculapius.ui.navigation.MainNavigation
import com.example.aesculapius.ui.profile.ProfileViewModel
import com.example.aesculapius.ui.theme.AesculapiusTheme
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        setContent {
            AesculapiusTheme {
                val profileViewModel: ProfileViewModel = hiltViewModel()
                val isUserRegistered = profileViewModel.isUserRegistered.collectAsState().value
                val morningReminder = profileViewModel.morningReminder.collectAsState().value
                val eveningReminder = profileViewModel.eveningReminder.collectAsState().value
                val ASTTestDate = profileViewModel.ASTTestDate.collectAsState().value
                val recommendationTest = profileViewModel.recommendationTest.collectAsState().value
                if (isUserRegistered) HomeScreen(
                    morningReminder = morningReminder,
                    eveningReminder = eveningReminder,
                    saveMorningReminder = { profileViewModel.saveMorningTime(it) },
                    saveEveningReminder = { profileViewModel.saveEveningTime(it) },
                    recommendationTestDate = recommendationTest,
                    ASTTestDate = ASTTestDate,
                    saveASTDate = { profileViewModel.saveASTTestDate(it) },
                    saveRecommendationDate = { profileViewModel.saveRecommendationTestDate(it) }
                )
                else MainNavigation(
                    morningReminder = morningReminder,
                    eveningReminder = eveningReminder,
                    saveMorningReminder = { profileViewModel.saveMorningTime(it) },
                    saveEveningReminder = { profileViewModel.saveEveningTime(it) },
                    onEndRegistration = { profileViewModel.changeUser(true) },
                    recommendationTestDate = recommendationTest,
                    ASTTestDate = ASTTestDate,
                    saveASTDate = { profileViewModel.saveASTTestDate(it) },
                    saveRecommendationDate = { profileViewModel.saveRecommendationTestDate(it) }
                )
            }
        }
    }
}