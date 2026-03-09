package com.davidpy.pizzacounter.data.entities

data class DaySummary(
    val dateKey: String,
    val totalCount: Int,
    val entries: List<PizzaEntry> = emptyList()
)
