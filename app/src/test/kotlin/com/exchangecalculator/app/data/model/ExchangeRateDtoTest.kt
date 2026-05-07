package com.exchangecalculator.app.data.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExchangeRateDtoTest {

    private fun dto(
        ask: String = "17.2550",
        bid: String = "17.2501",
        book: String = "usdc_mxn",
        date: String = "2026-05-07T00:19:29.752685791"
    ) = ExchangeRateDto(ask = ask, bid = bid, book = book, date = date)

    @Test
    fun `getCurrencyCode extracts uppercase code from book`() {
        assertEquals("MXN", dto(book = "usdc_mxn").getCurrencyCode())
        assertEquals("ARS", dto(book = "usdc_ars").getCurrencyCode())
        assertEquals("EUR", dto(book = "usdc_eur").getCurrencyCode())
        assertEquals("BRL", dto(book = "usdc_brl").getCurrencyCode())
        assertEquals("COP", dto(book = "usdc_cop").getCurrencyCode())
    }

    @Test
    fun `isValid returns true when all fields present`() {
        assertTrue(dto().isValid())
    }

    @Test
    fun `isValid returns false when book is empty`() {
        assertFalse(dto(book = "").isValid())
    }

    @Test
    fun `isValid returns false when ask is empty`() {
        assertFalse(dto(ask = "").isValid())
    }

    @Test
    fun `isValid returns false when bid is empty`() {
        assertFalse(dto(bid = "").isValid())
    }

    @Test
    fun `default constructor produces invalid dto`() {
        assertFalse(ExchangeRateDto().isValid())
    }
}
