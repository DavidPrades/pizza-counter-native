package com.davidpy.pizzacounter.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pizza_entries")
data class PizzaEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pizzaType: String,
    val dateKey: String,      // "yyyy-MM-dd"
    val timestampMillis: Long = System.currentTimeMillis()
)
