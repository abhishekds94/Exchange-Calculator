package com.exchangecalculator.app.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.exchangecalculator.app.presentation.screen.ErrorScreen
import com.exchangecalculator.app.presentation.screen.LoadingScreen
import com.exchangecalculator.app.presentation.screen.NoInternetScreen
import com.exchangecalculator.app.presentation.ui.theme.ExchangeCalculatorTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExchangeCalculatorScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun errorScreen_showsTitleAndMessage() {
        composeTestRule.setContent {
            ExchangeCalculatorTheme {
                ErrorScreen(
                    errorMessage = "Network error",
                    onRetry = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
        composeTestRule.onNodeWithText("Network error").assertIsDisplayed()
    }

    @Test
    fun errorScreen_retryButton_triggersCallback() {
        var retryClicked = false
        composeTestRule.setContent {
            ExchangeCalculatorTheme {
                ErrorScreen(
                    errorMessage = "Error",
                    onRetry = { retryClicked = true }
                )
            }
        }
        composeTestRule.onNodeWithText("Retry").performClick()
        assertTrue(retryClicked)
    }

    @Test
    fun noInternetScreen_showsTitle() {
        composeTestRule.setContent {
            ExchangeCalculatorTheme { NoInternetScreen(onRetry = {}) }
        }
        composeTestRule.onNodeWithText("No Internet Connection").assertIsDisplayed()
    }

    @Test
    fun noInternetScreen_showsBody() {
        composeTestRule.setContent {
            ExchangeCalculatorTheme { NoInternetScreen(onRetry = {}) }
        }
        composeTestRule
            .onNodeWithText("Please check your connection and try again")
            .assertIsDisplayed()
    }

    @Test
    fun noInternetScreen_retryButton_triggersCallback() {
        var retryClicked = false
        composeTestRule.setContent {
            ExchangeCalculatorTheme {
                NoInternetScreen(onRetry = { retryClicked = true })
            }
        }
        composeTestRule.onNodeWithText("Retry").performClick()
        assertTrue(retryClicked)
    }
}
