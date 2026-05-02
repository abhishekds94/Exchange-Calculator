package com.exchangecalculator.app.presentation.ui.theme

import android.os.Build
import android.view.View
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen
import androidx.core.view.WindowCompat

fun setupSplashScreen(view: View, splashScreen: SplashScreen) {
    splashScreen.setKeepOnScreenCondition { false }

    val window = (view.context as android.app.Activity).window
    val insetsController = WindowCompat.getInsetsController(window, view)

    if (insetsController != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        insetsController.isAppearanceLightStatusBars = false
    }
}

@Composable
fun SplashScreenContent() {
}
