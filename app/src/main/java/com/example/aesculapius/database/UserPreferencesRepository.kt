package com.example.aesculapius.database

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.aesculapius.ui.signup.SignUpUiState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("user_preferences")

/** [UserPreferencesRepository] репозиторий для DatastorePreferences */
@Singleton
class UserPreferencesRepository @Inject constructor(@ApplicationContext appContext: Context) {
    private val settingDataStore = appContext.dataStore

    val user: Flow<SignUpUiState> = settingDataStore.data.map { preferences ->
        SignUpUiState(
            name = preferences[NAME] ?: "",
            surname = preferences[SURNAME] ?: "",
            patronymic = preferences[PATRONYMIC] ?: "",
            birthday = LocalDate.parse(preferences[BIRTHDAY] ?: LocalDate.now().toString()),
            height = preferences[HEIGHT] ?: "",
            weight = preferences[WEIGHT] ?: ""
        )
    }

    val userId: Flow<String> = settingDataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences -> preferences[IS_USER_REGISTERED] ?: "" }

    val morningReminder: Flow<LocalDateTime> = settingDataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences -> Converters.stringToTime(preferences[MORNING_REMINDER_TIME]) }

    val eveningReminder: Flow<LocalDateTime> = settingDataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences -> Converters.stringToTime(preferences[EVENING_REMINDER_TIME]) }

    val astTest: Flow<String> = settingDataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences -> preferences[AST_TEST] ?: "" }

    val recommendationTest: Flow<String> = settingDataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences -> preferences[RECOMMENDATION_TEST] ?: "" }

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

    suspend fun saveUserData(userData: SignUpUiState) {
        settingDataStore.edit { preferences ->
            preferences[SURNAME] = userData.surname
            preferences[PATRONYMIC] = userData.patronymic
            preferences[NAME] = userData.name
            preferences[BIRTHDAY] = userData.birthday.toString()
            preferences[HEIGHT] = userData.height
            preferences[WEIGHT] = userData.weight
        }
    }

    suspend fun saveAstTestDate(astTestDate: String) {
        settingDataStore.edit { preferences ->
            preferences[AST_TEST] = astTestDate
        }
    }

    suspend fun saveRecommendationTest(recommendationTest: String) {
        settingDataStore.edit { preferences ->
            preferences[RECOMMENDATION_TEST] = recommendationTest
        }
    }

    suspend fun saveUserPreferences(userId: String) {
        settingDataStore.edit { preferences ->
            preferences[IS_USER_REGISTERED] = userId
        }
    }

    suspend fun saveUserMorningReminder(morningReminder: String) {
        settingDataStore.edit { preferences ->
            preferences[MORNING_REMINDER_TIME] = morningReminder
        }
    }

    suspend fun saveUserEveningReminder(eveningReminder: String) {
        settingDataStore.edit { preferences ->
            preferences[EVENING_REMINDER_TIME] = eveningReminder
        }
    }
}