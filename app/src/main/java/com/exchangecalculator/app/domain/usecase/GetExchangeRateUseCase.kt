package com.exchangecalculator.app.domain.usecase

import com.exchangecalculator.app.domain.model.Currency
import com.exchangecalculator.app.domain.model.Result
import com.exchangecalculator.app.data.repository.ICurrencyRepository
import javax.inject.Inject

class GetExchangeRateUseCase @Inject constructor(
    private val currencyRepository: ICurrencyRepository
) {
    suspend operator fun invoke(currencyCode: String): Result<Currency> {
        if (currencyCode.isBlank()) {
            return Result.Failure(
                IllegalArgumentException("Currency code cannot be empty")
            )
        }

        return try {
            currencyRepository.getExchangeRate(currencyCode)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}
