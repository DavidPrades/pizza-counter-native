package com.davidpy.pizzacounter.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "pizza_prefs")

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val LANGUAGE = stringPreferencesKey("language")
    }

    val isSoundEnabled: Flow<Boolean> = context.dataStore.data.map { it[SOUND_ENABLED] ?: true }
    val isVibrationEnabled: Flow<Boolean> = context.dataStore.data.map { it[VIBRATION_ENABLED] ?: true }
    val language: Flow<String> = context.dataStore.data.map { it[LANGUAGE] ?: "es" }

    suspend fun setSoundEnabled(enabled: Boolean) { context.dataStore.edit { it[SOUND_ENABLED] = enabled } }
    suspend fun setVibrationEnabled(enabled: Boolean) { context.dataStore.edit { it[VIBRATION_ENABLED] = enabled } }
    suspend fun setLanguage(lang: String) { context.dataStore.edit { it[LANGUAGE] = lang } }
}
