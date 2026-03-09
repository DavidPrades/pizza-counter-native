package com.davidpy.pizzacounter.data.dao

import androidx.room.*
import com.davidpy.pizzacounter.data.entities.PizzaEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface PizzaDao {
    @Query("SELECT * FROM pizza_entries WHERE dateKey = :dateKey ORDER BY timestampMillis DESC")
    fun getEntriesForDay(dateKey: String): Flow<List<PizzaEntry>>

    @Query("SELECT COUNT(*) FROM pizza_entries WHERE dateKey = :dateKey")
    fun getCountForDay(dateKey: String): Flow<Int>

    @Query("SELECT * FROM pizza_entries ORDER BY timestampMillis DESC")
    fun getAllEntries(): Flow<List<PizzaEntry>>

    @Query("SELECT COUNT(*) FROM pizza_entries")
    fun getTotalCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM pizza_entries WHERE dateKey BETWEEN :startDate AND :endDate")
    fun getCountInRange(startDate: String, endDate: String): Flow<Int>

    @Query("SELECT pizzaType FROM pizza_entries GROUP BY pizzaType ORDER BY COUNT(*) DESC LIMIT 1")
    fun getFavoritePizzaType(): Flow<String?>

    @Query("SELECT dateKey FROM pizza_entries GROUP BY dateKey ORDER BY COUNT(*) DESC LIMIT 1")
    fun getBestDay(): Flow<String?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: PizzaEntry)

    @Query("DELETE FROM pizza_entries")
    suspend fun deleteAll()
}
