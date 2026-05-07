package com.exchangecalculator.app.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.exchangecalculator.app.domain.model.Currency
import com.exchangecalculator.app.presentation.ui.theme.BackgroundColor
import com.exchangecalculator.app.presentation.ui.theme.GreenAccent
import com.exchangecalculator.app.presentation.ui.theme.TextPrimary
import com.exchangecalculator.app.presentation.viewmodel.ExchangeCalculatorUiState

@Composable
fun ExchangeCalculatorContent(
    uiState: ExchangeCalculatorUiState,
    onTopAmountChanged: (String) -> Unit,
    onCurrencySelected: (Currency) -> Unit,
    onSwap: () -> Unit,
    onErrorDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCurrencyPicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 24.dp)
            .padding(top = 56.dp)
    ) {
        Text(
            text = "Exchange calculator",
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                letterSpacing = (-0.6).sp
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        if (uiState.exchangeRateLabel.isNotEmpty()) {
            Text(
                text = uiState.exchangeRateLabel,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GreenAccent,
                    letterSpacing = 0.32.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                CurrencyInputCard(
                    currencyCode = uiState.topCurrencyCode,
                    flagEmoji = uiState.topCurrencyFlag,
                    amount = uiState.topRawDigits,
                    onAmountChanged = onTopAmountChanged,
                    isEnabled = uiState.isEnabled,
                    showSelector = uiState.topShowsDropdown,
                    onSelectorClick = { showCurrencyPicker = true }
                )

                Spacer(modifier = Modifier.height(16.dp))

                CurrencyOutputCard(
                    currencyCode = uiState.bottomCurrencyCode,
                    flagEmoji = uiState.bottomCurrencyFlag,
                    amount = uiState.bottomAmount,
                    showDropdown = uiState.bottomShowsDropdown,
                    onCurrencyClick = { showCurrencyPicker = true }
                )
            }

            SwapButton(
                onClick = onSwap,
                modifier = Modifier
                    .zIndex(1f)
                    .align(Alignment.Center)
            )
        }

        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            ErrorBanner(
                message = uiState.errorMessage,
                onDismiss = onErrorDismiss,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (showCurrencyPicker) {
        CurrencyPickerBottomSheet(
            currencies = uiState.availableCurrencies,
            selectedCurrency = uiState.selectedCurrency,
            onCurrencySelected = { currency ->
                onCurrencySelected(currency)
                showCurrencyPicker = false
            },
            onDismiss = { showCurrencyPicker = false }
        )
    }
}
