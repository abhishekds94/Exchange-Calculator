package com.exchangecalculator.app.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApi {

    @GET("tickers")
    suspend fun getExchangeRates(
        @Query("currencies") currencies: String
    ): Response<ResponseBody>

    @GET("tickers-currencies")
    suspend fun getAvailableCurrencyCodes(): Response<ResponseBody>
}
