package com.davidpy.pizzacounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.davidpy.pizzacounter.ads.AppodealManager
import com.davidpy.pizzacounter.ui.navigation.PizzaNavigation
import com.davidpy.pizzacounter.ui.navigation.theme.PizzaCounterTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appodealManager: AppodealManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Appodeal on startup
        appodealManager.initialize(this)

        setContent {
            PizzaCounterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PizzaNavigation()
                }
            }
        }
    }
}
