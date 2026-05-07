package com.exchangecalculator.app.presentation.viewmodel

import com.exchangecalculator.app.domain.model.Currency
import java.text.NumberFormat
import java.util.Locale

data class ExchangeCalculatorUiState(
    val availableCurrencies: List<Currency> = emptyList(),
    val selectedCurrency: Currency? = null,
    val usdcAmount: Double = 0.0,
    val usdcRawDigits: String = "",
    val otherAmount: Double = 0.0,
    val otherRawDigits: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isNetworkAvailable: Boolean = true,
    val isUsdcOnTop: Boolean = true
) {
    val isEnabled: Boolean get() = !isLoading

    val topCurrencyCode: String
        get() = if (isUsdcOnTop) "USDc" else selectedCurrency?.code ?: ""

    val topCurrencyFlag: String
        get() = if (isUsdcOnTop) "🇺🇸" else selectedCurrency?.flagEmoji ?: ""

    val topRawDigits: String
        get() = if (isUsdcOnTop) usdcRawDigits else otherRawDigits

    val topShowsDropdown: Boolean get() = !isUsdcOnTop

    val bottomCurrencyCode: String
        get() = if (isUsdcOnTop) selectedCurrency?.code ?: "" else "USDc"

    val bottomCurrencyFlag: String
        get() = if (isUsdcOnTop) selectedCurrency?.flagEmoji ?: "" else "🇺🇸"

    val bottomAmount: Double
        get() = if (isUsdcOnTop) otherAmount else usdcAmount

    val bottomShowsDropdown: Boolean get() = isUsdcOnTop

    val exchangeRateLabel: String
        get() = selectedCurrency?.let { currency ->
            if (!currency.isAvailable) return@let "Rate unavailable for ${currency.code}"

            val formatter = NumberFormat.getNumberInstance(Locale.US).apply {
                minimumFractionDigits = 2
                maximumFractionDigits = 4
            }

            if (isUsdcOnTop) {
                // USDc → other: use ask rate (buying)
                "1 USDc = ${formatter.format(currency.askRate)} ${currency.code}"
            } else {
                // other → USDc: use bid rate (selling)
                "1 USDc = ${formatter.format(currency.bidRate)} ${currency.code}"
            }
        } ?: ""
}