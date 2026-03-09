package com.davidpy.pizzacounter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidpy.pizzacounter.data.entities.PizzaEntry
import com.davidpy.pizzacounter.data.repository.PizzaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryDetailViewModel @Inject constructor(
    private val repository: PizzaRepository
) : ViewModel() {

    private val _entries = MutableStateFlow<List<PizzaEntry>>(emptyList())
    val entries: StateFlow<List<PizzaEntry>> = _entries.asStateFlow()

    fun loadDate(date: String) {
        viewModelScope.launch {
            repository.getAllEntries()
                .map { allEntries ->
                    allEntries.filter { it.dateKey == date }
                        .sortedByDescending { it.timestampMillis }
                }
                .collect {
                    _entries.value = it
                }
        }
    }
}
