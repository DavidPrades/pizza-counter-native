package com.davidpy.pizzacounter.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

object DateUtils {
    private val keyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    fun today(): String = LocalDate.now().format(keyFormatter)
    fun startOfWeek(): String = LocalDate.now().with(DayOfWeek.MONDAY).format(keyFormatter)
    fun startOfMonth(): String = LocalDate.now().withDayOfMonth(1).format(keyFormatter)
    fun formatForDisplay(dateKey: String): String {
        val date = LocalDate.parse(dateKey, keyFormatter)
        return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault()))
    }
    fun formatTime(millis: Long): String {
        val time = java.time.Instant.ofEpochMilli(millis)
            .atZone(java.time.ZoneId.systemDefault()).toLocalTime()
        return time.format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}
