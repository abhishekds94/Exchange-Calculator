package com.exchangecalculator.app.data.remote

import com.exchangecalculator.app.data.model.ExchangeRateDto
import kotlinx.serialization.InternalSerializationApi
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApi {
    @OptIn(InternalSerializationApi::class)
    @GET("tickers")
    suspend fun getExchangeRates(
        @Query("currencies") currencies: String
    ): List<ExchangeRateDto>
}
