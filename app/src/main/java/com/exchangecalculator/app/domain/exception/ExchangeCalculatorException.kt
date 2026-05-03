package com.exchangecalculator.app.domain.exception

sealed class ExchangeCalculatorException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

sealed class NetworkException(
    message: String,
    cause: Throwable? = null
) : ExchangeCalculatorException(message, cause)

class NoInternetException(
    message: String = "No internet connection",
    cause: Throwable? = null
) : NetworkException(message, cause)

class TimeoutException(
    message: String = "Request timed out",
    cause: Throwable? = null
) : NetworkException(message, cause)

class ServerException(
    message: String = "Server error",
    val statusCode: Int? = null,
    cause: Throwable? = null
) : NetworkException(message, cause)

class ApiException(
    message: String = "API error",
    cause: Throwable? = null
) : NetworkException(message, cause)

class InvalidDataException(
    message: String = "Invalid data received",
    cause: Throwable? = null
) : ExchangeCalculatorException(message, cause)

class InvalidAmountException(
    message: String = "Invalid amount entered",
    cause: Throwable? = null
) : ExchangeCalculatorException(message, cause)
