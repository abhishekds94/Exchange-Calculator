package com.exchangecalculator.app.domain.usecase

import com.exchangecalculator.app.domain.exception.InvalidAmountException
import com.exchangecalculator.app.domain.model.Result
import javax.inject.Inject

class ValidateAmountUseCase @Inject constructor() {

    operator fun invoke(amount: String): Result<Double> {
        return try {
            if (amount.isBlank()) {
                return Result.Success(0.0)
            }

            val parsedAmount = amount.toDoubleOrNull()
                ?: return Result.Failure(
                    InvalidAmountException("Invalid amount format")
                )

            if (parsedAmount < 0) {
                return Result.Failure(
                    InvalidAmountException("Amount cannot be negative")
                )
            }

            if (parsedAmount > 1_000_000_000) {
                return Result.Failure(
                    InvalidAmountException("Amount exceeds maximum limit")
                )
            }

            Result.Success(parsedAmount)
        } catch (e: Exception) {
            Result.Failure(
                InvalidAmountException("Error parsing amount: ${e.message}")
            )
        }
    }

    fun formatAmount(amount: Double): String {
        return if (amount == 0.0) {
            ""
        } else {
            amount.toString().trimTrailingZeros()
        }
    }
    private fun String.trimTrailingZeros(): String {
        return this.replace(Regex("(\\.\\d*?)0+$"), "$1")
            .replace(Regex("\\.$"), "")
    }
}
