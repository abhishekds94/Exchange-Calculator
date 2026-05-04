package com.exchangecalculator.app.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.exchangecalculator.app.domain.model.Currency
import com.exchangecalculator.app.presentation.viewmodel.ExchangeCalculatorUiState

@Composable
fun ExchangeCalculatorContent(
    uiState: ExchangeCalculatorUiState,
    onUsdcAmountChanged: (String) -> Unit,
    onOtherAmountChanged: (String) -> Unit,
    onCurrencySelected: (Currency) -> Unit,
    onSwap: () -> Unit,
    onErrorDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Exchange Calculator",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        CurrencyInputCard(
            label = "USDc",
            amount = uiState.usdcAmountInput,
            onAmountChanged = onUsdcAmountChanged,
            isEnabled = uiState.isEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onSwap,
                enabled = uiState.isEnabled,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small
                    )
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Swap currencies",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        CurrencyInputWithSelector(
            label = uiState.selectedCurrencyName,
            amount = uiState.otherAmountInput,
            onAmountChanged = onOtherAmountChanged,
            onCurrencyClick = {  },
            isEnabled = uiState.isEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        if (uiState.availableCurrencies.isNotEmpty()) {
            CurrencySelector(
                currencies = uiState.availableCurrencies,
                selectedCurrency = uiState.selectedCurrency,
                onCurrencySelected = onCurrencySelected,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        if (uiState.selectedCurrency != null) {
            ExchangeRateInfo(
                currency = uiState.selectedCurrency,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        if (uiState.errorMessage != null) {
            ErrorBanner(
                message = uiState.errorMessage,
                onDismiss = onErrorDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}