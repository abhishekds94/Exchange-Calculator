package com.exchangecalculator.app.domain.error

import com.exchangecalculator.app.domain.exception.InvalidDataException
import com.exchangecalculator.app.domain.exception.NoInternetException
import com.exchangecalculator.app.domain.exception.ServerException
import com.exchangecalculator.app.domain.exception.TimeoutException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ExchangeErrorMapper {
    fun toMessage(exception: Exception): String {
        return when (exception) {
            is NoInternetException -> "No internet connection. Please check your connection."
            is TimeoutException -> "Request timed out. Please try again."
            is ServerException -> "Server error. Please try again later."
            is InvalidDataException -> "Failed to load exchange rates."
            is UnknownHostException -> "No internet connection. Please check your connection."
            is SocketTimeoutException -> "Request timed out. Please try again."
            else -> exception.message ?: "Something went wrong."
        }
    }

    fun isNetworkError(exception: Exception): Boolean {
        return exception is NoInternetException ||
                exception is UnknownHostException ||
                exception is java.io.IOException
    }
}
