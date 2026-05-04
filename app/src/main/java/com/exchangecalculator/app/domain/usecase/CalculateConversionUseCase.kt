package com.exchangecalculator.app.domain.usecase

import com.exchangecalculator.app.domain.model.Result
import javax.inject.Inject

class CalculateConversionUseCase @Inject constructor() {
    operator fun invoke(
        usdcAmount: Double,
        exchangeRate: Double
    ): Result<Double> {
        return try {
            if (usdcAmount < 0) {
                return Result.Failure(
                    IllegalArgumentException("Amount cannot be negative")
                )
            }

            if (exchangeRate <= 0) {
                return Result.Failure(
                    IllegalArgumentException("Exchange rate must be positive")
                )
            }

            val convertedAmount = usdcAmount * exchangeRate
            Result.Success(convertedAmount)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    fun reverseConversion(
        amount: Double,
        exchangeRate: Double
    ): Result<Double> {
        return try {
            if (amount < 0) {
                return Result.Failure(
                    IllegalArgumentException("Amount cannot be negative")
                )
            }

            if (exchangeRate <= 0) {
                return Result.Failure(
                    IllegalArgumentException("Exchange rate must be positive")
                )
            }

            val convertedAmount = amount / exchangeRate
            Result.Success(convertedAmount)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}
