package com.exchangecalculator.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@kotlinx.serialization.InternalSerializationApi
@Serializable
data class ExchangeRateDto(
    @SerialName("ask")  val ask: String = "",
    @SerialName("bid")  val bid: String = "",
    @SerialName("book") val book: String = "",
    @SerialName("date") val date: String = ""
) {
    fun getCurrencyCode(): String =
        book.substringAfterLast("_").uppercase()

    fun isValid(): Boolean =
        book.isNotEmpty() && ask.isNotEmpty() && bid.isNotEmpty()
}