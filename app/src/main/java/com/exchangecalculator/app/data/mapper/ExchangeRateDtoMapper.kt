package com.exchangecalculator.app.data.mapper

import com.exchangecalculator.app.data.model.ExchangeRateDto
import com.exchangecalculator.app.domain.model.Currency
import kotlinx.serialization.InternalSerializationApi

object ExchangeRateDtoMapper {

    @OptIn(InternalSerializationApi::class)
    fun toCurrency(dto: ExchangeRateDto, currencyName: String): Currency {
        return Currency(
            code = dto.getCurrencyCode(),
            name = currencyName,
            exchangeRate = dto.getMidpointRate()
        )
    }

    @OptIn(InternalSerializationApi::class)
    fun toCurrencies(
        dtos: List<ExchangeRateDto>,
        currencyNames: Map<String, String>
    ): List<Currency> {
        return dtos.mapNotNull { dto ->
            val code = dto.getCurrencyCode()
            val name = currencyNames[code] ?: return@mapNotNull null
            toCurrency(dto, name)
        }
    }
}
