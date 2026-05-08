package com.exchangecalculator.app.domain.model

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val exception: Exception) : Result<Nothing>()

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Failure -> throw exception
    }

    inline fun <R> map(block: (T) -> R): Result<R> {
        return when (this) {
            is Success -> Success(block(data))
            is Failure -> Failure(exception)
        }
    }

    fun exceptionOrNull(): Exception? = when (this) {
        is Failure -> exception
        else -> null
    }

    fun isSuccess(): Boolean = this is Success

    fun isFailure(): Boolean = this is Failure
}
