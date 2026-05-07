package com.exchangecalculator.app.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.exchangecalculator.app.domain.model.Currency
import com.exchangecalculator.app.presentation.component.CurrencyPickerBottomSheet
import com.exchangecalculator.app.presentation.ui.theme.ExchangeCalculatorTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CurrencyPickerBottomSheetTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val currencies = listOf(
        Currency("MXN",  "Mexican Peso",   askRate = 17.26,   bidRate = 17.25,   flagEmoji = "🇲🇽"),
        Currency("ARS",  "Argentine Peso", askRate = 1465.24, bidRate = 1458.58, flagEmoji = "🇦🇷"),
        Currency("EURc", "Euro",           askRate = 0.0,     bidRate = 0.0,     flagEmoji = "🇪🇺", isAvailable = false)
    )

    @Test
    fun bottomSheet_showsTitle() {
        composeTestRule.setContent {
            ExchangeCalculatorTheme {
                CurrencyPickerBottomSheet(
                    currencies = currencies,
                    selectedCurrency = currencies[0],
                    onCurrencySelected = {},
                    onDismiss = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Choose currency").assertIsDisplayed()
    }

    @Test
    fun bottomSheet_showsAllCurrencyCodes() {
        composeTestRule.setContent {
            ExchangeCalculatorTheme {
                CurrencyPickerBottomSheet(
                    currencies = currencies,
                    selectedCurrency = null,
                    onCurrencySelected = {},
                    onDismiss = {}
                )
            }
        }
        currencies.forEach { currency ->
            composeTestRule.onNodeWithText(currency.code).assertIsDisplayed()
        }
    }

    @Test
    fun bottomSheet_selectingCurrency_callsCallback() {
        var selected: Currency? = null
        composeTestRule.setContent {
            ExchangeCalculatorTheme {
                CurrencyPickerBottomSheet(
                    currencies = currencies,
                    selectedCurrency = null,
                    onCurrencySelected = { selected = it },
                    onDismiss = {}
                )
            }
        }
        composeTestRule.onNodeWithText("ARS").performClick()
        assertEquals("ARS", selected?.code)
    }

    @Test
    fun bottomSheet_unavailableCurrency_showsRateUnavailable() {
        composeTestRule.setContent {
            ExchangeCalculatorTheme {
                CurrencyPickerBottomSheet(
                    currencies = currencies,
                    selectedCurrency = null,
                    onCurrencySelected = {},
                    onDismiss = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Rate unavailable").assertIsDisplayed()
    }

    @Test
    fun bottomSheet_closeButton_callsDismiss() {
        var dismissed = false
        composeTestRule.setContent {
            ExchangeCalculatorTheme {
                CurrencyPickerBottomSheet(
                    currencies = currencies,
                    selectedCurrency = currencies[0],
                    onCurrencySelected = {},
                    onDismiss = { dismissed = true }
                )
            }
        }
        composeTestRule.onNodeWithContentDescription("Close").performClick()
        assertTrue(dismissed)
    }
}
