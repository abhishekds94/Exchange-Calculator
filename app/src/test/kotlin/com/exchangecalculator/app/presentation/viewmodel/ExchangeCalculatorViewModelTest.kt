package com.exchangecalculator.app.presentation.viewmodel

import com.exchangecalculator.app.domain.model.Currency
import com.exchangecalculator.app.domain.model.Result
import com.exchangecalculator.app.domain.network.NetworkMonitor
import com.exchangecalculator.app.domain.repo.ICurrencyRepository
import com.exchangecalculator.app.domain.usecase.CalculateConversionUseCase
import com.exchangecalculator.app.domain.usecase.GetAvailableCurrenciesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ExchangeCalculatorViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var mockRepository: ICurrencyRepository
    @Mock
    private lateinit var mockNetworkMonitor: NetworkMonitor

    private lateinit var getAvailableCurrenciesUseCase: GetAvailableCurrenciesUseCase
    private lateinit var calculateConversionUseCase: CalculateConversionUseCase
    private lateinit var viewModel: ExchangeCalculatorViewModel

    private fun currency(
        code: String,
        askRate: Double = 17.26,
        bidRate: Double = 17.25,
        isAvailable: Boolean = true
    ) = Currency(
        code = code,
        name = "Test $code",
        askRate = askRate,
        bidRate = bidRate,
        flagEmoji = "🏳",
        isAvailable = isAvailable
    )

    private val testCurrencies = listOf(
        currency("MXN", askRate = 17.26, bidRate = 17.25),
        currency("ARS", askRate = 1465.24, bidRate = 1458.58),
        currency("COP", askRate = 3765.85, bidRate = 3725.06),
        currency("EURc", askRate = 0.0, bidRate = 0.0, isAvailable = false)
    )

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        whenever(mockNetworkMonitor.isConnected).thenReturn(MutableStateFlow(true))
        getAvailableCurrenciesUseCase = GetAvailableCurrenciesUseCase(mockRepository)
        calculateConversionUseCase = CalculateConversionUseCase()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = ExchangeCalculatorViewModel(
        getAvailableCurrenciesUseCase = getAvailableCurrenciesUseCase,
        calculateConversionUseCase = calculateConversionUseCase,
        networkMonitor = mockNetworkMonitor
    )

    // ── loadAvailableCurrencies ───────────────────────────────────────────────

    @Test
    fun `initial load shows loading then success`() = runTest {
        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Success(testCurrencies))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(testCurrencies.size, state.availableCurrencies.size)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }

    @Test
    fun `initial load failure sets error message`() = runTest {
        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Failure(Exception("Network error")))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.errorMessage)
    }

    @Test
    fun `retry clears error and reloads`() = runTest {
        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Failure(Exception("error")))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        assertNotNull(viewModel.uiState.value.errorMessage)

        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Success(testCurrencies))

        viewModel.retry()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.availableCurrencies.isNotEmpty())
        assertNull(viewModel.uiState.value.errorMessage)
    }

    // ── selectCurrency ────────────────────────────────────────────────────────

    @Test
    fun `selectCurrency updates selectedCurrency`() = runTest {
        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Success(testCurrencies))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val cop = testCurrencies[2]
        viewModel.selectCurrency(cop)

        assertEquals(cop.code, viewModel.uiState.value.selectedCurrency?.code)
    }

    @Test
    fun `selectCurrency with unavailable currency clears other amounts`() = runTest {
        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Success(testCurrencies))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val eur = testCurrencies[3] // isAvailable = false
        viewModel.selectCurrency(eur)

        assertEquals(0.0, viewModel.uiState.value.otherAmount, 0.0)
        assertEquals("", viewModel.uiState.value.otherRawDigits)
    }

    @Test
    fun `selectCurrency recalculates conversion using ask rate`() = runTest {
        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Success(testCurrencies))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onUsdcAmountChanged("100")
        testDispatcher.scheduler.advanceTimeBy(400)
        testDispatcher.scheduler.advanceUntilIdle()

        val mxn = testCurrencies[0] // askRate = 17.26
        viewModel.selectCurrency(mxn)

        // 100 × 17.26 = 1726.0
        assertEquals(1726.0, viewModel.uiState.value.otherAmount, 0.01)
    }

    // ── swapCurrencies ────────────────────────────────────────────────────────

    @Test
    fun `swapCurrencies flips isUsdcOnTop`() = runTest {
        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Success(testCurrencies))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isUsdcOnTop)
        viewModel.swapCurrencies()
        assertFalse(viewModel.uiState.value.isUsdcOnTop)
        viewModel.swapCurrencies()
        assertTrue(viewModel.uiState.value.isUsdcOnTop)
    }

    @Test
    fun `swapCurrencies does not change stored amounts`() = runTest {
        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Success(testCurrencies))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onUsdcAmountChanged("100")
        testDispatcher.scheduler.advanceTimeBy(400)
        testDispatcher.scheduler.advanceUntilIdle()

        val usdcBefore = viewModel.uiState.value.usdcAmount
        val otherBefore = viewModel.uiState.value.otherAmount

        viewModel.swapCurrencies()

        // Amounts stay in their fields — only position flag changes
        assertEquals(usdcBefore, viewModel.uiState.value.usdcAmount, 0.0)
        assertEquals(otherBefore, viewModel.uiState.value.otherAmount, 0.0)
    }

    @Test
    fun `multiple swaps return to original position`() = runTest {
        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Success(testCurrencies))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val initial = viewModel.uiState.value.isUsdcOnTop
        repeat(4) { viewModel.swapCurrencies() }
        assertEquals(initial, viewModel.uiState.value.isUsdcOnTop)
    }

    // ── UiState computed properties ───────────────────────────────────────────

    @Test
    fun `topCurrencyCode is USDc when isUsdcOnTop`() = runTest {
        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Success(testCurrencies))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("USDc", viewModel.uiState.value.topCurrencyCode)
    }

    @Test
    fun `topCurrencyCode is selectedCurrency after swap`() = runTest {
        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Success(testCurrencies))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.swapCurrencies()

        val expected = viewModel.uiState.value.selectedCurrency?.code
        assertEquals(expected, viewModel.uiState.value.topCurrencyCode)
    }

    @Test
    fun `bottomShowsDropdown is true when USDc on top`() = runTest {
        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Success(testCurrencies))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.bottomShowsDropdown)
    }

    @Test
    fun `topShowsDropdown is true after swap`() = runTest {
        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Success(testCurrencies))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.swapCurrencies()
        assertTrue(viewModel.uiState.value.topShowsDropdown)
    }

    @Test
    fun `exchangeRateLabel shows ask rate when USDc on top`() = runTest {
        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Success(testCurrencies))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val label = viewModel.uiState.value.exchangeRateLabel
        assertTrue("Label should contain USDc", label.contains("USDc"))
    }

    @Test
    fun `exchangeRateLabel changes direction after swap`() = runTest {
        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Success(testCurrencies))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val labelBefore = viewModel.uiState.value.exchangeRateLabel

        viewModel.swapCurrencies()

        val labelAfter = viewModel.uiState.value.exchangeRateLabel
        assertFalse(
            "Label should change direction after swap",
            labelBefore == labelAfter
        )
    }

    @Test
    fun `exchangeRateLabel shows unavailable for EURc`() = runTest {
        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Success(testCurrencies))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val eur = testCurrencies[3]
        viewModel.selectCurrency(eur)

        assertTrue(viewModel.uiState.value.exchangeRateLabel.contains("unavailable"))
    }

    // ── clearError ────────────────────────────────────────────────────────────

    @Test
    fun `clearError removes errorMessage`() = runTest {
        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Failure(Exception("error")))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.errorMessage)
        viewModel.clearError()
        assertNull(viewModel.uiState.value.errorMessage)
    }

    // ── network state ─────────────────────────────────────────────────────────

    @Test
    fun `network going offline updates isNetworkAvailable`() = runTest {
        val networkFlow = MutableStateFlow(true)
        whenever(mockNetworkMonitor.isConnected).thenReturn(networkFlow)
        whenever(mockRepository.getAvailableCurrencies())
            .thenReturn(Result.Success(testCurrencies))

        viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        networkFlow.value = false
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isNetworkAvailable)
    }
}
