package com.davidpy.pizzacounter.data.repository

import com.davidpy.pizzacounter.data.dao.PizzaDao
import com.davidpy.pizzacounter.data.entities.PizzaEntry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PizzaRepository @Inject constructor(private val dao: PizzaDao) {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun todayKey(): String = LocalDate.now().format(formatter)

    fun getEntriesForDay(dateKey: String): Flow<List<PizzaEntry>> =
        dao.getEntriesForDay(dateKey)

    fun getCountForDay(dateKey: String): Flow<Int> =
        dao.getCountForDay(dateKey)

    fun getAllEntries(): Flow<List<PizzaEntry>> =
        dao.getAllEntries()

    fun getTotalCount(): Flow<Int> =
        dao.getTotalCount()

    fun getCountInRange(startDate: String, endDate: String): Flow<Int> =
        dao.getCountInRange(startDate, endDate)

    fun getFavoritePizzaType(): Flow<String?> =
        dao.getFavoritePizzaType()

    fun getBestDay(): Flow<String?> =
        dao.getBestDay()

    suspend fun addPizza(type: String) {
        dao.insert(PizzaEntry(pizzaType = type, dateKey = todayKey()))
    }

    suspend fun clearAll() = dao.deleteAll()
}
