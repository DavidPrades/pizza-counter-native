package com.davidpy.pizzacounter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidpy.pizzacounter.data.entities.DaySummary
import com.davidpy.pizzacounter.data.repository.PizzaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: PizzaRepository
) : ViewModel() {

    val daySummaries: StateFlow<List<DaySummary>> = repository.getAllEntries()
        .map { entries ->
            entries.groupBy { it.dateKey }
                .map { (dateKey, dayEntries) ->
                    DaySummary(
                        dateKey = dateKey,
                        totalCount = dayEntries.size,
                        entries = dayEntries
                    )
                }
                .sortedByDescending { it.dateKey }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
