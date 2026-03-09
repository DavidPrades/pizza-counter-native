package com.davidpy.pizzacounter.ui.navigation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Pizza-themed colors
val PizzaOrange = Color(0xFFFF6B35)
val PizzaRed = Color(0xFFD32F2F)
val PizzaYellow = Color(0xFFFFC107)
val PizzaBrown = Color(0xFF795548)
val PizzaGreen = Color(0xFF4CAF50)

private val DarkColorScheme = darkColorScheme(
    primary = PizzaOrange,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF8B2500),
    onPrimaryContainer = Color(0xFFFFDBCB),
    secondary = PizzaYellow,
    onSecondary = Color.Black,
    background = Color(0xFF1A1A1A),
    surface = Color(0xFF2C2C2C),
    onBackground = Color.White,
    onSurface = Color.White,
    error = PizzaRed
)

private val LightColorScheme = lightColorScheme(
    primary = PizzaOrange,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDBCB),
    onPrimaryContainer = Color(0xFF3A0C00),
    secondary = PizzaBrown,
    onSecondary = Color.White,
    background = Color(0xFFFFF8F5),
    surface = Color.White,
    onBackground = Color(0xFF201A18),
    onSurface = Color(0xFF201A18),
    error = PizzaRed
)

@Composable
fun PizzaCounterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
