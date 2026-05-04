package com.exchangecalculator.app.data.repository

import com.exchangecalculator.app.domain.model.Currency
import com.exchangecalculator.app.domain.model.Result

interface CurrencyDataSource {
    suspend fun getAvailableCurrencies(): Result<List<Currency>>
    suspend fun getExchangeRate(currencyCode: String): Result<Currency>
    suspend fun getAllExchangeRates(): Result<List<Currency>>
}

class HardcodedCurrencyDataSource : CurrencyDataSource {

    private val hardcodedCurrencies = listOf(
        Currency(
            code = "MXN",
            name = "Mexican Peso",
            exchangeRate = 18.4087
        ),
        Currency(
            code = "ARS",
            name = "Argentine Peso",
            exchangeRate = 1545.2145
        ),
        Currency(
            code = "COP",
            name = "Colombian Peso",
            exchangeRate = 3832.42
        ),
        Currency(
            code = "EURc",
            name = "Euro",
            exchangeRate = 0.92
        ),
        Currency(
            code = "BRL",
            name = "Brazilian Real",
            exchangeRate = 4.97
        )
    )

    override suspend fun getAvailableCurrencies(): Result<List<Currency>> {
        return try {
            Result.Success(hardcodedCurrencies)
        } catch (e: Exception) {
            Result.Failure(e as? java.lang.Exception ?: Exception(e))
        }
    }

    override suspend fun getExchangeRate(currencyCode: String): Result<Currency> {
        return try {
            val currency = hardcodedCurrencies.find {
                it.code.equals(currencyCode, ignoreCase = true)
            }
            if (currency != null) {
                Result.Success(currency)
            } else {
                Result.Failure(
                    Exception("Currency $currencyCode not found")
                )
            }
        } catch (e: Exception) {
            Result.Failure(e as? java.lang.Exception ?: Exception(e))
        }
    }

    override suspend fun getAllExchangeRates(): Result<List<Currency>> {
        return getAvailableCurrencies()
    }
}

class NetworkCurrencyDataSource(
    private val exchangeRateApi: com.exchangecalculator.app.data.remote.ExchangeRateApi
) : CurrencyDataSource {

    override suspend fun getAvailableCurrencies(): Result<List<Currency>> {
        return HardcodedCurrencyDataSource().getAvailableCurrencies()
    }

    override suspend fun getExchangeRate(currencyCode: String): Result<Currency> {
        return HardcodedCurrencyDataSource().getExchangeRate(currencyCode)
    }

    override suspend fun getAllExchangeRates(): Result<List<Currency>> {
        return HardcodedCurrencyDataSource().getAllExchangeRates()
    }
}
