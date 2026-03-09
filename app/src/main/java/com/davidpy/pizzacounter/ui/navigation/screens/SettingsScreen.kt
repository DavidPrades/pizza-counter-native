package com.davidpy.pizzacounter.ui.navigation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.davidpy.pizzacounter.ui.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showClearDialog by remember { mutableStateOf(false) }
    var showLangDialog by remember { mutableStateOf(false) }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("¿Borrar historial?") },
            text = { Text("Se eliminarán todas las pizzas registradas. Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearHistory()
                    showClearDialog = false
                }) { Text("Sí, borrar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("Cancelar") }
            }
        )
    }

    if (showLangDialog) {
        AlertDialog(
            onDismissRequest = { showLangDialog = false },
            title = { Text("Idioma") },
            text = {
                Column {
                    listOf("es" to "🇪🇸 Español", "en" to "🇬🇧 English").forEach { (code, label) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = state.language == code,
                                onClick = {
                                    viewModel.setLanguage(code)
                                    showLangDialog = false
                                }
                            )
                            Text(text = label)
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        SettingsSectionTitle("Preferencias")

        SettingsToggleItem(
            icon = Icons.Default.VolumeUp,
            title = "Sonido",
            checked = state.soundEnabled,
            onCheckedChange = viewModel::setSoundEnabled
        )
        SettingsToggleItem(
            icon = Icons.Default.Vibration,
            title = "Vibración",
            checked = state.vibrationEnabled,
            onCheckedChange = viewModel::setVibrationEnabled
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        SettingsSectionTitle("General")

        SettingsClickItem(
            icon = Icons.Default.Language,
            title = "Idioma",
            subtitle = if (state.language == "es") "🇪🇸 Español" else "🇬🇧 English",
            onClick = { showLangDialog = true }
        )

        SettingsClickItem(
            icon = Icons.Default.DeleteForever,
            title = "Borrar historial",
            subtitle = "Eliminar todos los registros",
            onClick = { showClearDialog = true },
            isDestructive = true
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        SettingsSectionTitle("Acerca de")

        SettingsInfoItem(
            icon = Icons.Default.Info,
            title = "Versión",
            value = "1.0.0"
        )
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
    )
}

@Composable
fun SettingsToggleItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(16.dp))
        Text(text = title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun SettingsClickItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isDestructive: Boolean = false
) {
    val titleColor = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .let { mod ->
                mod.then(
                    Modifier.padding(0.dp) // clickable handled by container
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null,
            tint = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge, color = titleColor)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        TextButton(onClick = onClick) {
            Text(if (isDestructive) "Borrar" else "Cambiar", color = titleColor)
        }
    }
}

@Composable
fun SettingsInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.width(16.dp))
        Text(text = title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
