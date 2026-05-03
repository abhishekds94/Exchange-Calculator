package com.exchangecalculator.app.domain.model

data class Currency(
    val code: String,
    val name: String,
    val exchangeRate: Double
) {
    override fun toString(): String = "$code - $name"
}

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Failure -> throw exception
        Loading -> throw IllegalStateException("Result is still loading")
    }
}
