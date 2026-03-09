package com.davidpy.pizzacounter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidpy.pizzacounter.data.repository.PizzaRepository
import com.davidpy.pizzacounter.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val language: String = "es"
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: PreferencesManager,
    private val repository: PizzaRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        preferences.isSoundEnabled,
        preferences.isVibrationEnabled,
        preferences.language
    ) { sound, vibration, lang ->
        SettingsUiState(sound, vibration, lang)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsUiState())

    fun setSoundEnabled(enabled: Boolean) = viewModelScope.launch { preferences.setSoundEnabled(enabled) }
    fun setVibrationEnabled(enabled: Boolean) = viewModelScope.launch { preferences.setVibrationEnabled(enabled) }
    fun setLanguage(lang: String) = viewModelScope.launch { preferences.setLanguage(lang) }
    fun clearHistory() = viewModelScope.launch { repository.clearAll() }
}
