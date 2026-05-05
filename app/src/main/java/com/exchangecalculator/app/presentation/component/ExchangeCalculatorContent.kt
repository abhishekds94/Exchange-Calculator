package com.exchangecalculator.app.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var showCurrencyPicker by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Exchange calculator",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            uiState.selectedCurrency?.let { currency ->
                val formattedRate = String.format("%.2f", currency.exchangeRate)
                Text(
                    text = "1 USDc = $formattedRate ${currency.code}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }

            CurrencyInputCard(
                currencyCode = "USDc",
                amount = uiState.usdcAmountInput,
                onAmountChanged = onUsdcAmountChanged,
                isEnabled = uiState.isEnabled,
                showSelector = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = onSwap,
                    enabled = uiState.isEnabled,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.small
                        )
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapVert,
                        contentDescription = "Swap currencies",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            CurrencyInputCard(
                currencyCode = uiState.selectedCurrencyName,
                amount = uiState.otherAmountInput,
                onAmountChanged = onOtherAmountChanged,
                isEnabled = uiState.isEnabled,
                showSelector = true,
                onSelectorClick = { showCurrencyPicker = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            if (uiState.errorMessage != null) {
                ErrorBanner(
                    message = uiState.errorMessage,
                    onDismiss = onErrorDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (showCurrencyPicker) {
            CurrencyPickerBottomSheet(
                currencies = uiState.availableCurrencies,
                selectedCurrency = uiState.selectedCurrency,
                onCurrencySelected = onCurrencySelected,
                onDismiss = { showCurrencyPicker = false }
            )
        }
    }
}
