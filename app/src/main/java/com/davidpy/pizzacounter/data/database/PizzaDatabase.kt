package com.davidpy.pizzacounter.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.davidpy.pizzacounter.data.dao.PizzaDao
import com.davidpy.pizzacounter.data.entities.PizzaEntry

@Database(entities = [PizzaEntry::class], version = 1, exportSchema = false)
abstract class PizzaDatabase : RoomDatabase() {
    abstract fun pizzaDao(): PizzaDao

    companion object {
        const val DATABASE_NAME = "pizza_database"
    }
}
