package com.exchangecalculator.app.domain.repo

import com.exchangecalculator.app.domain.model.Currency
import com.exchangecalculator.app.domain.model.Result

interface ICurrencyRepository {
    suspend fun getAvailableCurrencies(): Result<List<Currency>>
    suspend fun getExchangeRate(currencyCode: String): Result<Currency>
}
