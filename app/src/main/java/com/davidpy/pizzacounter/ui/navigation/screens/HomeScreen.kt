package com.davidpy.pizzacounter.ui.navigation.screens

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidpy.pizzacounter.ui.components.*
import com.davidpy.pizzacounter.ui.viewmodels.HomeViewModel
import com.davidpy.pizzacounter.domain.PizzaType
import com.davidpy.pizzacounter.utils.DateUtils

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as? Activity

    // Vibrate on pizza add
    fun vibrate() {
        if (!state.vibrationEnabled) return
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    // Achievement dialog
    state.unlockedAchievement?.let { achievement ->
        AchievementDialog(achievement = achievement, onDismiss = viewModel::dismissAchievement)
    }

    // Pizza type dialog
    if (state.showPizzaTypeDialog) {
        PizzaTypeSelectorDialog(
            onTypeSelected = { type ->
                viewModel.addPizza(type, activity!!)
                vibrate()
                viewModel.dismissPizzaTypeDialog()
            },
            onDismiss = viewModel::dismissPizzaTypeDialog
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Pizzas hoy",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))

        AnimatedPizzaCounter(count = state.todayCount)

        Spacer(Modifier.height(48.dp))

        AddPizzaButton(
            onClick = { viewModel.showPizzaTypeDialog() },
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(horizontal = 8.dp)
        )

        Spacer(Modifier.height(32.dp))

        // Recent entries
        if (state.todayEntries.isNotEmpty()) {
            Text(
                text = "Últimas pizzas de hoy",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            state.todayEntries.takeLast(3).reversed().forEach { entry ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val type = PizzaType.fromString(entry.pizzaType)
                    Text(text = type.emoji, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "${type.displayName} · ${DateUtils.formatTime(entry.timestampMillis)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
