package com.davidpy.pizzacounter.domain

enum class PizzaType(val displayName: String, val emoji: String) {
    MARGARITA("Margarita", "🍕"),
    PEPPERONI("Pepperoni", "🍕"),
    CUATRO_QUESOS("Cuatro Quesos", "🧀"),
    BARBACOA("Barbacoa", "🔥"),
    HAWAIANA("Hawaiana", "🍍"),
    VEGETARIANA("Vegetariana", "🥦"),
    CARBONARA("Carbonara", "🥚");

    companion object {
        fun fromString(name: String): PizzaType =
            entries.firstOrNull { it.name == name } ?: MARGARITA
    }
}
