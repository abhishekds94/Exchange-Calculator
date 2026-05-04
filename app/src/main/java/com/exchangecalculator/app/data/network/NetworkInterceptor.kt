package com.exchangecalculator.app.data.network

import okhttp3.Interceptor
import okhttp3.Response

class NetworkInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()
            .header("Accept", "application/json")
            .header("User-Agent", "ExchangeCalculator/1.0")
            .header("Cache-Control", "max-age=300") // 5 minutes cache

        val request = requestBuilder.build()

        return try {
            chain.proceed(request)
        } catch (e: Exception) {
            throw e
        }
    }
}
