package com.exchangecalculator.app.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.exchangecalculator.app.presentation.component.ExchangeCalculatorContent
import com.exchangecalculator.app.presentation.component.LoadingOverlay
import com.exchangecalculator.app.presentation.viewmodel.ExchangeCalculatorViewModel

@Composable
fun ExchangeCalculatorScreen(
    viewModel: ExchangeCalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState().value

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.errorMessage != null && uiState.availableCurrencies.isEmpty() -> {
                    ErrorScreen(
                        errorMessage = uiState.errorMessage,
                        onRetry = { viewModel.retry() }
                    )
                }

                !uiState.isNetworkAvailable && uiState.availableCurrencies.isEmpty() -> {
                    NoInternetScreen(
                        onRetry = { viewModel.retry() }
                    )
                }

                else -> {
                    ExchangeCalculatorContent(
                        uiState = uiState,
                        onUsdcAmountChanged = { viewModel.onUsdcAmountChanged(it) },
                        onOtherAmountChanged = { viewModel.onOtherAmountChanged(it) },
                        onCurrencySelected = { viewModel.selectCurrency(it) },
                        onSwap = { viewModel.swapCurrencies() },
                        onErrorDismiss = { viewModel.clearError() }
                    )
                }
            }

            LoadingOverlay(
                isVisible = uiState.isLoading,
                message = "Loading exchange rates..."
            )
        }
    }
}
