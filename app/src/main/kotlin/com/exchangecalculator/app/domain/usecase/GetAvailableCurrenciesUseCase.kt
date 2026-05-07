package com.exchangecalculator.app.domain.usecase

import com.exchangecalculator.app.domain.model.Currency
import com.exchangecalculator.app.domain.model.Result
import com.exchangecalculator.app.domain.repo.ICurrencyRepository
import javax.inject.Inject

class GetAvailableCurrenciesUseCase @Inject constructor(
    private val repository: ICurrencyRepository
) {
    suspend operator fun invoke(): Result<List<Currency>> =
        repository.getAvailableCurrencies()
}
