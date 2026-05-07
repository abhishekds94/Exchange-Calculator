package com.exchangecalculator.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.exchangecalculator.app.presentation.navigation.ExchangeCalculatorNavGraph
import com.exchangecalculator.app.presentation.ui.theme.ExchangeCalculatorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { false }
        setContent {
            ExchangeCalculatorTheme {
                ExchangeCalculatorNavGraph(
                    navController = rememberNavController()
                )
            }
        }
    }
}
