package com.davidpy.pizzacounter.ui.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidpy.pizzacounter.ads.AppodealManager
import com.davidpy.pizzacounter.data.repository.PizzaRepository
import com.davidpy.pizzacounter.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class StatsUiState(
    val totalPizzas: Int = 0,
    val weekPizzas: Int = 0,
    val monthPizzas: Int = 0,
    val favoritePizza: String? = null,
    val bestDay: String? = null
)

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val repository: PizzaRepository,
    private val appodealManager: AppodealManager
) : ViewModel() {

    val uiState: StateFlow<StatsUiState> = combine(
        repository.getTotalCount(),
        repository.getCountInRange(DateUtils.startOfWeek(), DateUtils.today()),
        repository.getCountInRange(DateUtils.startOfMonth(), DateUtils.today()),
        repository.getFavoritePizzaType(),
        repository.getBestDay()
    ) { total, week, month, fav, bestDay ->
        StatsUiState(total, week, month, fav, bestDay)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatsUiState())

    fun onScreenOpened(activity: Activity) {
        appodealManager.onStatsOpened(activity)
    }
}
