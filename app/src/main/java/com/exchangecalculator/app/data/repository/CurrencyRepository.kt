package com.exchangecalculator.app.data.repository

import com.exchangecalculator.app.domain.model.Currency
import com.exchangecalculator.app.domain.model.Result

interface ICurrencyRepository {
    suspend fun getAvailableCurrencies(): Result<List<Currency>>

    suspend fun getExchangeRate(currencyCode: String): Result<Currency>

    suspend fun getAllExchangeRates(): Result<List<Currency>>
}

class CurrencyRepositoryImpl(
    private val dataSource: CurrencyDataSource = HardcodedCurrencyDataSource()
) : ICurrencyRepository {

    override suspend fun getAvailableCurrencies(): Result<List<Currency>> {
        return dataSource.getAvailableCurrencies()
    }

    override suspend fun getExchangeRate(currencyCode: String): Result<Currency> {
        return dataSource.getExchangeRate(currencyCode)
    }

    override suspend fun getAllExchangeRates(): Result<List<Currency>> {
        return dataSource.getAllExchangeRates()
    }
}
