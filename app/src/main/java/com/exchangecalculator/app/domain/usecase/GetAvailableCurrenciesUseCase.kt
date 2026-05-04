package com.exchangecalculator.app.domain.usecase

import com.exchangecalculator.app.domain.model.Currency
import com.exchangecalculator.app.domain.model.Result
import com.exchangecalculator.app.data.repository.ICurrencyRepository
import javax.inject.Inject

class GetAvailableCurrenciesUseCase @Inject constructor(
    private val currencyRepository: ICurrencyRepository
) {
    suspend operator fun invoke(): Result<List<Currency>> {
        return try {
            currencyRepository.getAvailableCurrencies()
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}
