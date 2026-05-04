package com.exchangecalculator.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.exchangecalculator.app.presentation.screen.ExchangeCalculatorScreen
import com.exchangecalculator.app.presentation.ui.theme.ExchangeCalculatorTheme
import com.exchangecalculator.app.presentation.viewmodel.ExchangeCalculatorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: ExchangeCalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { false }

        setContent {
            ExchangeCalculatorTheme {
                ExchangeCalculatorScreen(viewModel = viewModel)
            }
        }
    }
}
