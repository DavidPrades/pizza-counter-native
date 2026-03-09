package com.davidpy.pizzacounter.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val KEY_SOUND = booleanPreferencesKey("sound_enabled")
        val KEY_VIBRATION = booleanPreferencesKey("vibration_enabled")
        val KEY_LANGUAGE = stringPreferencesKey("language")
        val KEY_ACHIEVEMENTS = stringPreferencesKey("unlocked_achievements")
    }

    val soundEnabled: Flow<Boolean> = dataStore.data.map { it[KEY_SOUND] ?: true }
    val vibrationEnabled: Flow<Boolean> = dataStore.data.map { it[KEY_VIBRATION] ?: true }
    val language: Flow<String> = dataStore.data.map { it[KEY_LANGUAGE] ?: "es" }
    val unlockedAchievements: Flow<Set<String>> = dataStore.data.map {
        it[KEY_ACHIEVEMENTS]?.split(",")?.filter { s -> s.isNotBlank() }?.toSet() ?: emptySet()
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        dataStore.edit { it[KEY_SOUND] = enabled }
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        dataStore.edit { it[KEY_VIBRATION] = enabled }
    }

    suspend fun setLanguage(lang: String) {
        dataStore.edit { it[KEY_LANGUAGE] = lang }
    }

    suspend fun unlockAchievement(id: String) {
        dataStore.edit { prefs ->
            val current = prefs[KEY_ACHIEVEMENTS] ?: ""
            val set = current.split(",").filter { it.isNotBlank() }.toMutableSet()
            set.add(id)
            prefs[KEY_ACHIEVEMENTS] = set.joinToString(",")
        }
    }
}
