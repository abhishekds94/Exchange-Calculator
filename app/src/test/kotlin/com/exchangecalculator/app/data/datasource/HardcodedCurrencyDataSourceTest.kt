package com.exchangecalculator.app.data.datasource

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HardcodedCurrencyDataSourceTest {

    private lateinit var dataSource: HardcodedCurrencyDataSource

    @Before
    fun setup() {
        dataSource = HardcodedCurrencyDataSource()
    }

    @Test
    fun `supportedCurrencies contains exactly 5 currencies`() {
        assertEquals(5, dataSource.supportedCurrencies.size)
    }

    @Test
    fun `currencyCodes returns display codes not api codes`() {
        val codes = dataSource.currencyCodes
        assertTrue("Should contain EURc not EUR", codes.contains("EURc"))
        assertTrue(codes.contains("MXN"))
        assertTrue(codes.contains("ARS"))
        assertTrue(codes.contains("BRL"))
        assertTrue(codes.contains("COP"))
    }

    @Test
    fun `apiCurrencyCodes returns api codes not display codes`() {
        val codes = dataSource.apiCurrencyCodes
        assertTrue("Should contain EUR not EURc", codes.contains("EUR"))
        assertTrue(codes.contains("MXN"))
        assertTrue(codes.contains("ARS"))
        assertTrue(codes.contains("BRL"))
        assertTrue(codes.contains("COP"))
        assertFalse("Should NOT contain EURc", codes.contains("EURc"))
    }

    @Test
    fun `EURc display code maps to EUR api code`() {
        val eur = dataSource.supportedCurrencies.find { it.code == "EURc" }
        assertNotNull(eur)
        assertEquals("EUR", eur!!.apiCode)
    }

    @Test
    fun `getMetadata finds by display code`() {
        val result = dataSource.getMetadata("MXN")
        assertNotNull(result)
        assertEquals("MXN", result!!.code)
        assertEquals("Mexican Peso", result.name)
    }

    @Test
    fun `getMetadata finds EURc by display code`() {
        val result = dataSource.getMetadata("EURc")
        assertNotNull(result)
        assertEquals("EURc", result!!.code)
        assertEquals("EUR", result.apiCode)
    }

    @Test
    fun `getMetadata finds EURc by api code EUR`() {
        val result = dataSource.getMetadata("EUR")
        assertNotNull(result)
        assertEquals("EURc", result!!.code)
    }

    @Test
    fun `getMetadata is case insensitive`() {
        assertNotNull(dataSource.getMetadata("mxn"))
        assertNotNull(dataSource.getMetadata("MXN"))
        assertNotNull(dataSource.getMetadata("Mxn"))
    }

    @Test
    fun `getMetadata returns null for unknown code`() {
        assertNull(dataSource.getMetadata("XYZ"))
        assertNull(dataSource.getMetadata("USD"))
        assertNull(dataSource.getMetadata(""))
    }

    @Test
    fun `all currencies have non-empty flagEmoji`() {
        dataSource.supportedCurrencies.forEach { currency ->
            assertTrue(
                "Flag emoji missing for ${currency.code}",
                currency.flagEmoji.isNotEmpty()
            )
        }
    }

    @Test
    fun `currencyCodes and apiCurrencyCodes have same size`() {
        assertEquals(dataSource.currencyCodes.size, dataSource.apiCurrencyCodes.size)
    }
}
