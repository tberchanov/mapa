package com.application.mapa.util

import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.KEY_ROUTE

@Composable
inline fun ComponentActivity.ObserveOnBackPressed(
    route: String,
    navController: NavHostController,
    crossinline onBackPressed: () -> Unit
) {
    onBackPressedDispatcher.addCallback {
        val currentRoute = navController.currentDestination
            ?.arguments?.get(KEY_ROUTE)?.defaultValue as? String
        if (currentRoute == route) {
            onBackPressed()
        }
    }
}