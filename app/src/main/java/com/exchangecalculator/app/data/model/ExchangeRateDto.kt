package com.exchangecalculator.app.data.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.InternalSerializationApi
data class ExchangeRateDto(
    @SerialName("ask")
    val ask: String,

    @SerialName("bid")
    val bid: String,

    @SerialName("book")
    val book: String,

    @SerialName("date")
    val date: String
) {
    /**
     * Calculate midpoint rate between ask and bid
     */
    fun getMidpointRate(): Double {
        return ((ask.toDoubleOrNull() ?: 0.0) + (bid.toDoubleOrNull() ?: 0.0)) / 2
    }

    fun getCurrencyCode(): String {
        return book.substringAfterLast("_").uppercase()
    }
}

@kotlinx.serialization.InternalSerializationApi
data class ExchangeRatesResponse(
    @SerialName("data")
    val data: List<ExchangeRateDto> = emptyList()
)
