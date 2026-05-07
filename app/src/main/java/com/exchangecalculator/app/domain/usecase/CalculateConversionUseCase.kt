package com.exchangecalculator.app.domain.usecase

import com.exchangecalculator.app.domain.model.Currency
import com.exchangecalculator.app.domain.model.Result
import javax.inject.Inject

class CalculateConversionUseCase @Inject constructor() {

    /**
     * USDc → other currency
     * Uses ASK rate — this is what you pay when buying the currency
     */
    operator fun invoke(usdcAmount: Double, currency: Currency): Result<Double> {
        if (usdcAmount < 0) return Result.Failure(IllegalArgumentException("Amount cannot be negative"))
        if (currency.askRate <= 0) return Result.Failure(IllegalArgumentException("Ask rate must be positive"))
        return Result.Success(usdcAmount * currency.askRate)
    }

    /**
     * other currency → USDc
     * Uses BID rate — this is what the market pays when you sell the currency
     */
    fun reverseConversion(amount: Double, currency: Currency): Result<Double> {
        if (amount < 0) return Result.Failure(IllegalArgumentException("Amount cannot be negative"))
        if (currency.bidRate <= 0) return Result.Failure(IllegalArgumentException("Bid rate must be positive"))
        return Result.Success(amount / currency.bidRate)
    }
}
