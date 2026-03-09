package com.davidpy.pizzacounter.domain

data class Achievement(
    val id: String,
    val title: String,
    val emoji: String,
    val requiredCount: Int
)

object Achievements {
    val all = listOf(
        Achievement("first", "¡Primera pizza!", "🍕", 1),
        Achievement("ten", "¡10 pizzas!", "🍕", 10),
        Achievement("fifty", "¡50 pizzas!", "🏆", 50),
        Achievement("hundred", "¡100 pizzas! ¡Leyenda!", "👑", 100)
    )

    fun checkUnlocked(total: Int, previous: Int): Achievement? =
        all.firstOrNull { it.requiredCount in (previous + 1)..total }
}
