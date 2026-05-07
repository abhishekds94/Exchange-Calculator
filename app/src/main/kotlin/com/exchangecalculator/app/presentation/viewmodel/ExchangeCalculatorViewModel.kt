package com.exchangecalculator.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exchangecalculator.app.domain.error.ExchangeErrorMapper
import com.exchangecalculator.app.domain.model.Currency
import com.exchangecalculator.app.domain.model.Result
import com.exchangecalculator.app.domain.network.NetworkMonitor
import com.exchangecalculator.app.domain.usecase.CalculateConversionUseCase
import com.exchangecalculator.app.domain.usecase.GetAvailableCurrenciesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeCalculatorViewModel @Inject constructor(
    private val getAvailableCurrenciesUseCase: GetAvailableCurrenciesUseCase,
    private val calculateConversionUseCase: CalculateConversionUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeCalculatorUiState())
    val uiState: StateFlow<ExchangeCalculatorUiState> = _uiState.asStateFlow()

    private var calculationJob: Job? = null

    init {
        observeNetworkState()
        loadAvailableCurrencies()
    }

    private fun observeNetworkState() {
        viewModelScope.launch {
            networkMonitor.isConnected.collectLatest { isConnected ->
                updateUiState { copy(isNetworkAvailable = isConnected) }
            }
        }
    }

    private fun loadAvailableCurrencies() {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true, errorMessage = null, isNetworkError = false) }
            when (val result = getAvailableCurrenciesUseCase()) {
                is Result.Success -> {
                    val first = result.data.firstOrNull()
                    updateUiState {
                        copy(
                            availableCurrencies = result.data,
                            isLoading = false,
                            selectedCurrency = first,
                            isNetworkError = false
                        )
                    }
                    first?.let { selectCurrency(it) }
                }

                is Result.Failure -> {
                    val networkError = ExchangeErrorMapper.isNetworkError(result.exception)
                    updateUiState {
                        copy(
                            isLoading = false,
                            isNetworkError = networkError,
                            errorMessage = if (networkError) null
                            else ExchangeErrorMapper.toMessage(result.exception)
                        )
                    }
                }

                Result.Loading -> {}
            }
        }
    }

    fun onTopAmountChanged(rawDigits: String) {
        if (_uiState.value.isUsdcOnTop) onUsdcAmountChanged(rawDigits)
        else onOtherAmountChanged(rawDigits)
    }

    fun onUsdcAmountChanged(rawDigits: String) {
        _uiState.value = _uiState.value.copy(usdcRawDigits = rawDigits)
        calculationJob?.cancel()
        calculationJob = viewModelScope.launch {
            delay(300)
            val amount = rawDigits.toDoubleOrNull() ?: 0.0
            _uiState.value = _uiState.value.copy(usdcAmount = amount)
            val currency = _uiState.value.selectedCurrency ?: return@launch

            when (val result = calculateConversionUseCase(amount, currency)) {
                is Result.Success -> _uiState.value = _uiState.value.copy(
                    otherAmount = result.data,
                    otherRawDigits = formatRaw(result.data),
                    errorMessage = null
                )

                is Result.Failure -> _uiState.value = _uiState.value.copy(
                    errorMessage = "Invalid amount"
                )

                Result.Loading -> {}
            }
        }
    }

    fun onOtherAmountChanged(rawDigits: String) {
        _uiState.value = _uiState.value.copy(otherRawDigits = rawDigits)
        calculationJob?.cancel()
        calculationJob = viewModelScope.launch {
            delay(300)
            val amount = rawDigits.toDoubleOrNull() ?: 0.0
            _uiState.value = _uiState.value.copy(otherAmount = amount)
            val currency = _uiState.value.selectedCurrency ?: return@launch

            when (val result = calculateConversionUseCase.reverseConversion(amount, currency)) {
                is Result.Success -> _uiState.value = _uiState.value.copy(
                    usdcAmount = result.data,
                    usdcRawDigits = formatRaw(result.data),
                    errorMessage = null
                )

                is Result.Failure -> _uiState.value = _uiState.value.copy(
                    errorMessage = "Invalid amount"
                )

                Result.Loading -> {}
            }
        }
    }

    fun selectCurrency(currency: Currency) {
        _uiState.value = _uiState.value.copy(
            selectedCurrency = currency,
            otherAmount = if (currency.isAvailable) _uiState.value.otherAmount else 0.0,
            otherRawDigits = if (currency.isAvailable) _uiState.value.otherRawDigits else ""
        )
        if (!currency.isAvailable) return

        val state = _uiState.value
        when (val result = calculateConversionUseCase(state.usdcAmount, currency)) {
            is Result.Success -> _uiState.value = _uiState.value.copy(
                otherAmount = result.data,
                otherRawDigits = formatRaw(result.data),
                errorMessage = null
            )

            is Result.Failure -> _uiState.value = _uiState.value.copy(
                errorMessage = "Error calculating conversion"
            )

            Result.Loading -> {}
        }
    }

    fun swapCurrencies() {
        _uiState.value = _uiState.value.copy(
            isUsdcOnTop = !_uiState.value.isUsdcOnTop
        )
    }

    fun clearError() = updateUiState { copy(errorMessage = null) }

    fun retry() = loadAvailableCurrencies()

    private fun formatRaw(value: Double): String =
        String.format("%.2f", value).trimEnd('0').trimEnd('.')

    private fun updateUiState(
        block: ExchangeCalculatorUiState.() -> ExchangeCalculatorUiState
    ) {
        _uiState.value = _uiState.value.block()
    }
}
