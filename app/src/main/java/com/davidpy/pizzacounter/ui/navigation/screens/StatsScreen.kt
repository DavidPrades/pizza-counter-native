package com.davidpy.pizzacounter.ui.navigation.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidpy.pizzacounter.domain.PizzaType
import com.davidpy.pizzacounter.ui.components.StatCard
import com.davidpy.pizzacounter.ui.viewmodels.StatsViewModel
import com.davidpy.pizzacounter.utils.DateUtils

@Composable
fun StatsScreen(viewModel: StatsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as? Activity

    LaunchedEffect(Unit) {
        activity?.let { viewModel.onScreenOpened(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero stat
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "🍕", fontSize = 56.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    text = state.totalPizzas.toString(),
                    style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "pizzas en total",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Week / Month cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                label = "Esta semana",
                value = state.weekPizzas.toString(),
                emoji = "📅",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Este mes",
                value = state.monthPizzas.toString(),
                emoji = "📆",
                modifier = Modifier.weight(1f)
            )
        }

        // Favorite + Best day
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val favType = state.favoritePizza?.let { PizzaType.fromString(it) }
            StatCard(
                label = "Favorita",
                value = favType?.displayName ?: "—",
                emoji = favType?.emoji ?: "🍕",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Mejor día",
                value = state.bestDay?.let { DateUtils.formatForDisplay(it) } ?: "—",
                emoji = "🏆",
                modifier = Modifier.weight(1f)
            )
        }

        // Motivational message
        if (state.totalPizzas > 0) {
            val message = when {
                state.totalPizzas >= 100 -> "¡Eres una auténtica leyenda pizzera! 👑"
                state.totalPizzas >= 50 -> "¡50 pizzas! ¡Vas camino de la gloria! 🏆"
                state.totalPizzas >= 10 -> "¡Ya vas por ${state.totalPizzas} pizzas! 🚀"
                else -> "¡Buen comienzo, sigue así! 💪"
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(20.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}
