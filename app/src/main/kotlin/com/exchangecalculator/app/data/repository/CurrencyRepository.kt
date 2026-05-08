package com.exchangecalculator.app.data.repository

import com.exchangecalculator.app.data.datasource.CurrencyDataSource
import com.exchangecalculator.app.domain.model.Currency
import com.exchangecalculator.app.domain.model.Result
import com.exchangecalculator.app.domain.repo.ICurrencyRepository
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val dataSource: CurrencyDataSource
) : ICurrencyRepository {

    override suspend fun getAvailableCurrencies(): Result<List<Currency>> =
        dataSource.getAvailableCurrencies()

    override suspend fun getExchangeRate(currencyCode: String): Result<Currency> =
        dataSource.getExchangeRate(currencyCode)
}
