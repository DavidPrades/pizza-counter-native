package com.davidpy.pizzacounter.ads

import android.app.Activity
import android.util.Log
import com.appodeal.ads.Appodeal
import com.appodeal.ads.initializing.ApdInitializationCallback
import com.appodeal.ads.initializing.ApdInitializationError
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppodealManager @Inject constructor() {

    companion object {
        private const val APP_KEY = "TU_APPODEAL_APP_KEY_AQUI"
        private const val TAG = "AppodealManager"
    }

    private val adTypes = Appodeal.BANNER or Appodeal.INTERSTITIAL

    private var interstitialCount = 0
    private val interstitialThreshold = 5

    fun initialize(activity: Activity) {
        Appodeal.setTesting(true)
        Appodeal.initialize(activity, APP_KEY, adTypes, object : ApdInitializationCallback {
            override fun onInitializationFinished(errors: List<ApdInitializationError>?) {
                if (errors.isNullOrEmpty()) {
                    Log.d(TAG, "Appodeal initialized successfully")
                } else {
                    errors.forEach { Log.e(TAG, "Init error: ${it.message}") }
                }
            }
        })
    }

    fun showBanner(activity: Activity) {
        if (Appodeal.isLoaded(Appodeal.BANNER)) {
            Appodeal.show(activity, Appodeal.BANNER)
        }
    }

    fun onPizzaAdded(activity: Activity) {
        interstitialCount++
        if (interstitialCount >= interstitialThreshold) {
            showInterstitial(activity)
            interstitialCount = 0
        }
    }

    fun onStatsOpened(activity: Activity) {
        showInterstitial(activity)
    }

    private fun showInterstitial(activity: Activity) {
        if (Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
            Appodeal.show(activity, Appodeal.INTERSTITIAL)
        } else {
            Log.d(TAG, "Interstitial not ready yet")
        }
    }
}
