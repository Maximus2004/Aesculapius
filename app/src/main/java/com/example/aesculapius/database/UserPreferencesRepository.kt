package com.example.aesculapius.database

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.aesculapius.ui.signup.SignUpUiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("user_preferences")

/** [UserPreferencesRepository] репозиторий для DataStorePreferences */
@Singleton
class UserPreferencesRepository @Inject constructor(@ApplicationContext appContext: Context) {
    private val settingDataStore = appContext.dataStore

    val user: Flow<SignUpUiState> = settingDataStore.data.map { preferences ->
        SignUpUiState(
            id = preferences[IS_USER_REGISTERED] ?: "",
            name = preferences[NAME] ?: "",
            surname = preferences[SURNAME] ?: "",
            patronymic = preferences[PATRONYMIC] ?: "",
            birthday = LocalDate.parse(preferences[BIRTHDAY] ?: LocalDate.now().toString()),
            height = preferences[HEIGHT] ?: "",
            weight = preferences[WEIGHT] ?: "",
            morningReminder = Converters.stringToTime(preferences[MORNING_REMINDER_TIME]),
            eveningReminder = Converters.stringToTime(preferences[EVENING_REMINDER_TIME]),
            astTestDate = preferences[AST_TEST] ?: "",
            recommendationTestDate = preferences[RECOMMENDATION_TEST] ?: ""
        )
    }

    private companion object {
        val IS_USER_REGISTERED = stringPreferencesKey("is_user_registered")
        val MORNING_REMINDER_TIME = stringPreferencesKey("morning_reminder_time")
        val EVENING_REMINDER_TIME = stringPreferencesKey("evening_reminder_time")
        val AST_TEST = stringPreferencesKey("ast_test")
        val RECOMMENDATION_TEST = stringPreferencesKey("recommendation_test")

        val SURNAME = stringPreferencesKey("surname")
        val PATRONYMIC = stringPreferencesKey("patronymic")
        val NAME = stringPreferencesKey("name")
        val BIRTHDAY = stringPreferencesKey("birthday")
        val HEIGHT = stringPreferencesKey("height")
        val WEIGHT = stringPreferencesKey("weight")
    }

    /**
     * [saveUserData] переносит параметры из SignUpUiState в
     * DotsStore Preferences для дальнейшего редактирования в профиле
     */
    suspend fun saveUserData(signUpUiState: SignUpUiState) {
        settingDataStore.edit { preferences ->
            preferences[IS_USER_REGISTERED] = signUpUiState.id!!
            preferences[SURNAME] = signUpUiState.surname
            preferences[PATRONYMIC] = signUpUiState.patronymic
            preferences[NAME] = signUpUiState.name
            preferences[BIRTHDAY] = signUpUiState.birthday.toString()
            preferences[HEIGHT] = signUpUiState.height
            preferences[WEIGHT] = signUpUiState.weight
            preferences[MORNING_REMINDER_TIME] = Converters.timeToString(signUpUiState.morningReminder)
            preferences[EVENING_REMINDER_TIME] = Converters.timeToString(signUpUiState.eveningReminder)
        }
    }

    /**
     * [saveAstTestDate] сохраняет полную дату следующего прохождения АСТ теста
     */
    suspend fun saveAstTestDate(astTestDate: String) {
        settingDataStore.edit { preferences ->
            preferences[AST_TEST] = astTestDate
        }
    }

    /**
     * [saveRecommendationTest] сохраняет полную дату следующего прохождения Теста приверженности
     */
    suspend fun saveRecommendationTest(recommendationTest: String) {
        settingDataStore.edit { preferences ->
            preferences[RECOMMENDATION_TEST] = recommendationTest
        }
    }

    /**
     * [saveUserMorningReminder] сохраняет полную дату и
     * время следующего утреннего внесения метрик
     */
    suspend fun saveUserMorningReminder(morningReminder: String) {
        settingDataStore.edit { preferences ->
            preferences[MORNING_REMINDER_TIME] = morningReminder
        }
    }

    /**
     * [saveUserEveningReminder] сохраняет полную дату и
     * время следующего вечернего внесения метрик
     */
    suspend fun saveUserEveningReminder(eveningReminder: String) {
        settingDataStore.edit { preferences ->
            preferences[EVENING_REMINDER_TIME] = eveningReminder
        }
    }
}