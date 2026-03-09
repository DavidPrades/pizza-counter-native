package com.davidpy.pizzacounter.ui.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidpy.pizzacounter.ads.AppodealManager
import com.davidpy.pizzacounter.data.entities.PizzaEntry
import com.davidpy.pizzacounter.data.repository.PizzaRepository
import com.davidpy.pizzacounter.domain.Achievement
import com.davidpy.pizzacounter.domain.Achievements
import com.davidpy.pizzacounter.domain.PizzaType
import com.davidpy.pizzacounter.utils.DateUtils
import com.davidpy.pizzacounter.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val todayCount: Int = 0,
    val todayEntries: List<PizzaEntry> = emptyList(),
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val unlockedAchievement: Achievement? = null,
    val showPizzaTypeDialog: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PizzaRepository,
    private val preferences: PreferencesManager,
    private val appodealManager: AppodealManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var previousTotal = 0

    init {
        val today = DateUtils.today()

        viewModelScope.launch {
            combine(
                repository.getCountForDay(today),
                repository.getEntriesForDay(today),
                preferences.isSoundEnabled,
                preferences.isVibrationEnabled
            ) { count, entries, sound, vibration ->
                HomeUiState(
                    todayCount = count,
                    todayEntries = entries,
                    soundEnabled = sound,
                    vibrationEnabled = vibration
                )
            }.collect { state -> _uiState.value = state }
        }

        viewModelScope.launch {
            repository.getTotalCount().collect { total ->
                val achievement = Achievements.checkUnlocked(total, previousTotal)
                if (achievement != null) {
                    _uiState.update { it.copy(unlockedAchievement = achievement) }
                }
                previousTotal = total
            }
        }
    }

    fun showPizzaTypeDialog() {
        _uiState.update { it.copy(showPizzaTypeDialog = true) }
    }

    fun dismissPizzaTypeDialog() {
        _uiState.update { it.copy(showPizzaTypeDialog = false) }
    }

    fun addPizza(type: PizzaType, activity: Activity) {
        viewModelScope.launch {
            repository.addPizza(type.name)
            appodealManager.onPizzaAdded(activity)
        }
    }

    fun dismissAchievement() {
        _uiState.update { it.copy(unlockedAchievement = null) }
    }
}
