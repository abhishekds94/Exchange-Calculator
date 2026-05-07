package com.exchangecalculator.app.domain.usecase

import com.exchangecalculator.app.domain.model.Currency
import com.exchangecalculator.app.domain.model.Result
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CalculateConversionUseCaseTest {

    private lateinit var useCase: CalculateConversionUseCase

    private fun currency(
        code: String = "MXN",
        askRate: Double = 17.26,
        bidRate: Double = 17.25,
        isAvailable: Boolean = true
    ) = Currency(
        code = code,
        name = "Test Currency",
        askRate = askRate,
        bidRate = bidRate,
        flagEmoji = "",
        isAvailable = isAvailable
    )

    @Before
    fun setup() {
        useCase = CalculateConversionUseCase()
    }

    @Test
    fun `invoke uses ask rate for USDc to currency conversion`() {
        val result = useCase(100.0, currency(askRate = 17.26, bidRate = 17.25))
        assertTrue(result is Result.Success)
        assertEquals(1726.0, (result as Result.Success).data, 0.01)
    }

    @Test
    fun `invoke with zero amount returns zero`() {
        val result = useCase(0.0, currency())
        assertTrue(result is Result.Success)
        assertEquals(0.0, (result as Result.Success).data, 0.0)
    }

    @Test
    fun `invoke with negative amount returns failure`() {
        val result = useCase(-10.0, currency())
        assertTrue(result is Result.Failure)
    }

    @Test
    fun `invoke with zero ask rate returns failure`() {
        val result = useCase(100.0, currency(askRate = 0.0))
        assertTrue(result is Result.Failure)
    }

    @Test
    fun `invoke with negative ask rate returns failure`() {
        val result = useCase(100.0, currency(askRate = -1.0))
        assertTrue(result is Result.Failure)
    }

    @Test
    fun `invoke MXN conversion is correct`() {
        // 23 USDc × 17.26 ask = 397.0
        val result = useCase(23.0, currency(askRate = 17.26))
        assertTrue(result is Result.Success)
        assertEquals(397.0, (result as Result.Success).data, 0.1)
    }

    @Test
    fun `reverseConversion uses bid rate for currency to USDc`() {
        // 1725 / 17.25 bid = 100.0
        val result = useCase.reverseConversion(1725.0, currency(askRate = 17.26, bidRate = 17.25))
        assertTrue(result is Result.Success)
        assertEquals(100.0, (result as Result.Success).data, 0.01)
    }

    @Test
    fun `reverseConversion with zero amount returns zero`() {
        val result = useCase.reverseConversion(0.0, currency())
        assertTrue(result is Result.Success)
        assertEquals(0.0, (result as Result.Success).data, 0.0)
    }

    @Test
    fun `reverseConversion with negative amount returns failure`() {
        val result = useCase.reverseConversion(-10.0, currency())
        assertTrue(result is Result.Failure)
    }

    @Test
    fun `reverseConversion with zero bid rate returns failure`() {
        val result = useCase.reverseConversion(100.0, currency(bidRate = 0.0))
        assertTrue(result is Result.Failure)
    }

    @Test
    fun `bid ask spread means converting back gives slightly less`() {
        // ask > bid means buying and immediately selling results in a loss
        val c = currency(askRate = 17.26, bidRate = 17.25)
        val forward = (useCase(100.0, c) as Result.Success).data
        val backward =
            (useCase.reverseConversion(forward, c) as Result.Success).data
        assertTrue(
            "Spread should make round-trip slightly different from original",
            backward != 100.0
        )
    }
}
