package com.exchangecalculator.app.data.datasource

import javax.inject.Inject

class HardcodedCurrencyDataSource @Inject constructor() {

    val supportedCurrencies = listOf(
        SupportedCurrency(code = "MXN", apiCode = "MXN", name = "Mexican Peso", flagEmoji = "🇲🇽"),
        SupportedCurrency(code = "ARS", apiCode = "ARS", name = "Argentine Peso", flagEmoji = "🇦🇷"),
        SupportedCurrency(code = "BRL", apiCode = "BRL", name = "Brazilian Real", flagEmoji = "🇧🇷"),
        SupportedCurrency(code = "COP", apiCode = "COP", name = "Colombian Peso", flagEmoji = "🇨🇴"),
        SupportedCurrency(code = "EURc", apiCode = "EUR", name = "Euro", flagEmoji = "🇪🇺"),
    )

    val currencyCodes: List<String>
        get() = supportedCurrencies.map { it.code }

    val apiCurrencyCodes: List<String>
        get() = supportedCurrencies.map { it.apiCode }

    fun getMetadata(code: String): SupportedCurrency? =
        supportedCurrencies.find {
            it.code.equals(code, ignoreCase = true) ||
                    it.apiCode.equals(code, ignoreCase = true)
        }

    data class SupportedCurrency(
        val code: String,
        val apiCode: String,
        val name: String,
        val flagEmoji: String
    )
}
