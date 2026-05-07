package com.exchangecalculator.app.data.datasource

import com.exchangecalculator.app.domain.model.Currency
import com.exchangecalculator.app.domain.model.Result

interface CurrencyDataSource {
    suspend fun getAvailableCurrencies(): Result<List<Currency>>
    suspend fun getExchangeRate(currencyCode: String): Result<Currency>
    suspend fun getAllExchangeRates(): Result<List<Currency>> // for future usage when API returns data
}
