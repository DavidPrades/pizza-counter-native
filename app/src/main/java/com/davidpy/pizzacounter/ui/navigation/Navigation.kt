package com.davidpy.pizzacounter.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.davidpy.pizzacounter.ui.navigation.screens.*

sealed class Screen(val route: String, val label: String = "", val icon: ImageVector? = null) {
    object Home : Screen("home", "Inicio", Icons.Default.Home)
    object History : Screen("history", "Historial", Icons.Default.History)
    object HistoryDetail : Screen("history_detail/{date}") {
        fun createRoute(date: String) = "history_detail/$date"
    }
    object Stats : Screen("stats", "Estadísticas", Icons.Default.BarChart)
    object Settings : Screen("settings", "Ajustes", Icons.Default.Settings)
}

val bottomNavItems = listOf(Screen.Home, Screen.History, Screen.Stats, Screen.Settings)

@Composable
fun PizzaNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            val showBottomBar = bottomNavItems.any { it.route == currentDestination?.route }
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { screen.icon?.let { Icon(it, contentDescription = screen.label) } },
                            label = { Text(screen.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { 
                HomeScreen() 
            }
            composable(Screen.History.route) { 
                HistoryScreen(
                    onDayClick = { date ->
                        navController.navigate(Screen.HistoryDetail.createRoute(date))
                    }
                ) 
            }
            composable(
                route = Screen.HistoryDetail.route,
                arguments = listOf(navArgument("date") { type = NavType.StringType })
            ) { backStackEntry ->
                val date = backStackEntry.arguments?.getString("date") ?: ""
                HistoryDetailScreen(
                    date = date,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Stats.route) { 
                StatsScreen() 
            }
            composable(Screen.Settings.route) { 
                SettingsScreen() 
            }
        }
    }
}
