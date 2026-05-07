package com.exchangecalculator.app.data.datasource

import com.exchangecalculator.app.data.model.ExchangeRateDto
import com.exchangecalculator.app.data.remote.ExchangeRateApi
import com.exchangecalculator.app.domain.model.Currency
import com.exchangecalculator.app.domain.model.Result
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import javax.inject.Inject

class NetworkCurrencyDataSource @Inject constructor(
    private val api: ExchangeRateApi,
    private val json: Json,
    private val hardcodedSource: HardcodedCurrencyDataSource
) : CurrencyDataSource {

    override suspend fun getAvailableCurrencies(): Result<List<Currency>> {
        return try {
            val apiCodes = fetchApiCurrencyCodes()

            val response = api.getExchangeRates(
                currencies = apiCodes.joinToString(",")
            )

            if (!response.isSuccessful) {
                return Result.Failure(
                    Exception("HTTP ${response.code()}: ${response.errorBody()?.string()}")
                )
            }

            val rawJson = response.body()?.string()
                ?: return Result.Failure(Exception("Empty response from server"))

            val jsonArray = json.parseToJsonElement(rawJson).jsonArray

            val ratesByApiCode = mutableMapOf<String, ExchangeRateDto>()
            jsonArray.forEach { element ->
                if (element !is JsonNull) {
                    val dto = json.decodeFromJsonElement<ExchangeRateDto>(element)
                    if (dto.isValid()) {
                        ratesByApiCode[dto.getCurrencyCode()] = dto
                    }
                }
            }

            // Map ALL supported currencies — mark unavailable if API returned null
            val currencies = hardcodedSource.supportedCurrencies.map { metadata ->
                val dto = ratesByApiCode[metadata.apiCode]
                Currency(
                    code = metadata.code,
                    name = metadata.name,
                    askRate = dto?.ask?.toDoubleOrNull() ?: 0.0,
                    bidRate = dto?.bid?.toDoubleOrNull() ?: 0.0,
                    flagEmoji = metadata.flagEmoji,
                    isAvailable = dto != null   // false for EUR (null from API)
                )
            }

            Result.Success(currencies)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun getExchangeRate(currencyCode: String): Result<Currency> {
        return try {
            val metadata = hardcodedSource.getMetadata(currencyCode)
                ?: return Result.Failure(Exception("Unknown currency: $currencyCode"))

            val response = api.getExchangeRates(currencies = metadata.apiCode)

            if (!response.isSuccessful) {
                return Result.Failure(
                    Exception("HTTP ${response.code()}: ${response.errorBody()?.string()}")
                )
            }

            val rawJson = response.body()?.string()
                ?: return Result.Failure(Exception("Empty response"))

            val jsonArray = json.parseToJsonElement(rawJson).jsonArray
            val dto = jsonArray
                .filter { it !is JsonNull }
                .map { json.decodeFromJsonElement<ExchangeRateDto>(it) }
                .firstOrNull { it.isValid() }

            Result.Success(
                Currency(
                    code = metadata.code,
                    name = metadata.name,
                    askRate = dto?.ask?.toDoubleOrNull() ?: 0.0,
                    bidRate = dto?.bid?.toDoubleOrNull() ?: 0.0,
                    flagEmoji = metadata.flagEmoji,
                    isAvailable = dto != null
                )
            )
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun getAllExchangeRates(): Result<List<Currency>> =
        getAvailableCurrencies()

    private suspend fun fetchApiCurrencyCodes(): List<String> {
        return try {
            val response = api.getAvailableCurrencyCodes()
            if (!response.isSuccessful) return hardcodedSource.apiCurrencyCodes
            val rawJson = response.body()?.string()
                ?: return hardcodedSource.apiCurrencyCodes
            val codes = json.decodeFromString<List<String>>(rawJson)
            codes.ifEmpty { hardcodedSource.apiCurrencyCodes }
        } catch (e: Exception) {
            hardcodedSource.apiCurrencyCodes
        }
    }
}