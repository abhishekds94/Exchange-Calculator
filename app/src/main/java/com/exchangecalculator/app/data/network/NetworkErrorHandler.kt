package com.exchangecalculator.app.data.network

import com.exchangecalculator.app.domain.exception.ApiException
import com.exchangecalculator.app.domain.exception.ExchangeCalculatorException
import com.exchangecalculator.app.domain.exception.InvalidDataException
import com.exchangecalculator.app.domain.exception.NoInternetException
import com.exchangecalculator.app.domain.exception.ServerException
import com.exchangecalculator.app.domain.exception.TimeoutException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object NetworkErrorHandler {

    fun handleException(
        exception: Exception,
        isNetworkAvailable: Boolean
    ): ExchangeCalculatorException {
        return when (exception) {
            // Network connectivity issues
            is UnknownHostException -> NoInternetException(
                message = "Unable to reach server. Check your internet connection.",
                cause = exception
            )

            is NoInternetException -> exception

            // Request timeout
            is SocketTimeoutException -> TimeoutException(
                message = "Request took too long. Please try again.",
                cause = exception
            )

            is TimeoutException -> exception

            // HTTP errors
            is HttpException -> {
                when (exception.code()) {
                    in 400..499 -> ServerException(
                        message = "Request error (${exception.code()}). Please check your input.",
                        statusCode = exception.code(),
                        cause = exception
                    )
                    in 500..599 -> ServerException(
                        message = "Server error (${exception.code()}). Please try again later.",
                        statusCode = exception.code(),
                        cause = exception
                    )
                    else -> ApiException(
                        message = "API error (${exception.code()})",
                        cause = exception
                    )
                }
            }

            // Serialization/data parsing errors
            is SerializationException -> InvalidDataException(
                message = "Failed to parse response data.",
                cause = exception
            )

            // Generic IO errors
            is IOException -> {
                if (!isNetworkAvailable) {
                    NoInternetException(cause = exception)
                } else {
                    ApiException(
                        message = "Network error. Please check your connection.",
                        cause = exception
                    )
                }
            }

            // Already a domain exception
            is ExchangeCalculatorException -> exception

            // Unknown error
            else -> ApiException(
                message = exception.message ?: "An unexpected error occurred",
                cause = exception
            )
        }
    }

    fun getErrorMessage(exception: ExchangeCalculatorException): String {
        return exception.message ?: "An unknown error occurred"
    }
}
