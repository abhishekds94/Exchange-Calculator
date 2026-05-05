package com.exchangecalculator.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.exchangecalculator.app.presentation.screen.ExchangeCalculatorScreen
import com.exchangecalculator.app.presentation.viewmodel.ExchangeCalculatorViewModel

object NavDestinations {
    const val CALCULATOR = "calculator"
}

object DeepLinks {
    const val BASE_URL = "exchangecalculator://app"
    const val CALCULATOR = "$BASE_URL/calculator"
}

@Composable
fun ExchangeCalculatorNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NavDestinations.CALCULATOR
    ) {
        composable(
            route = NavDestinations.CALCULATOR,
            deepLinks = listOf(
                androidx.navigation.navDeepLink {
                    uriPattern = DeepLinks.CALCULATOR
                }
            )
        ) {
            val viewModel: ExchangeCalculatorViewModel = hiltViewModel()
            ExchangeCalculatorScreen(viewModel = viewModel)
        }
    }
}
