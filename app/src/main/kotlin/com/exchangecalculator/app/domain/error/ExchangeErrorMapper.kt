package com.exchangecalculator.app.domain.error

import com.exchangecalculator.app.domain.exception.InvalidDataException
import com.exchangecalculator.app.domain.exception.NoInternetException
import com.exchangecalculator.app.domain.exception.ServerException
import com.exchangecalculator.app.domain.exception.TimeoutException

object ExchangeErrorMapper {
    fun toMessage(exception: Exception): String {
        return when (exception) {
            is NoInternetException -> "No internet connection. Please check your connection."
            is TimeoutException -> "Request timed out. Please try again."
            is ServerException -> "Server error. Please try again later."
            is InvalidDataException -> "Failed to load exchange rates."
            else -> exception.message ?: "Something went wrong."
        }
    }
}
