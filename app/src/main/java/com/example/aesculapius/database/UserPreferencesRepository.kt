package com.example.aesculapius.database

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("user_preferences")

@Singleton
class UserPreferencesRepository @Inject constructor(@ApplicationContext appContext: Context) {
    private val settingDataStore = appContext.dataStore

    val isUserRegistered: Flow<Boolean> = settingDataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences -> preferences[IS_USER_REGISTERED] ?: false }

    val morningReminder: Flow<LocalTime> = settingDataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences -> Converters.stringToTime(preferences[MORNING_REMINDER_TIME]) }

    val eveningReminder: Flow<LocalTime> = settingDataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences -> Converters.stringToTime(preferences[EVENING_REMINDER_TIME]) }

    private companion object {
        val IS_USER_REGISTERED = booleanPreferencesKey("is_user_registered")
        val MORNING_REMINDER_TIME = stringPreferencesKey("morning_reminder_time")
        val EVENING_REMINDER_TIME = stringPreferencesKey("evening_reminder_time")
    }

    suspend fun saveUserPreferences(isUserRegistered: Boolean) {
        settingDataStore.edit { preferences ->
            preferences[IS_USER_REGISTERED] = isUserRegistered
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