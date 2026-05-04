package com.exchangecalculator.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exchangecalculator.app.data.network.NetworkErrorHandler
import com.exchangecalculator.app.data.network.NetworkStateManager
import com.exchangecalculator.app.domain.model.Currency
import com.exchangecalculator.app.domain.model.Result
import com.exchangecalculator.app.domain.usecase.CalculateConversionUseCase
import com.exchangecalculator.app.domain.usecase.GetAvailableCurrenciesUseCase
import com.exchangecalculator.app.domain.usecase.GetExchangeRateUseCase
import com.exchangecalculator.app.domain.usecase.ValidateAmountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val getExchangeRateUseCase: GetExchangeRateUseCase,
    private val calculateConversionUseCase: CalculateConversionUseCase,
    private val validateAmountUseCase: ValidateAmountUseCase,
    private val networkStateManager: NetworkStateManager
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow(ExchangeCalculatorUiState())
    val uiState: StateFlow<ExchangeCalculatorUiState> = _uiState.asStateFlow()

    // Network state
    private var isNetworkAvailable = true

    init {
        observeNetworkState()
        loadAvailableCurrencies()
    }

    private fun observeNetworkState() {
        viewModelScope.launch {
            networkStateManager.isConnected.collectLatest { isConnected ->
                isNetworkAvailable = isConnected
                updateUiState {
                    copy(isNetworkAvailable = isConnected)
                }
            }
        }
    }

    private fun loadAvailableCurrencies() {
        viewModelScope.launch {
            updateUiState { copy(isLoading = true) }

            when (val result = getAvailableCurrenciesUseCase()) {
                is Result.Success -> {
                    updateUiState {
                        copy(
                            availableCurrencies = result.data,
                            isLoading = false,
                            selectedCurrency = result.data.firstOrNull()
                        )
                    }
                    // Set first currency as default
                    result.data.firstOrNull()?.let { selectCurrency(it) }
                }

                is Result.Failure -> {
                    val exception = result.exception
                    val errorMessage = NetworkErrorHandler.getErrorMessage(
                        NetworkErrorHandler.handleException(exception, isNetworkAvailable)
                    )
                    updateUiState {
                        copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }
                }

                Result.Loading -> {
                    // Handled above
                }
            }
        }
    }

    fun onUsdcAmountChanged(amount: String) {
        updateUiState { copy(usdcAmountInput = amount) }

        viewModelScope.launch {
            delay(300)

            when (val validationResult = validateAmountUseCase(amount)) {
                is Result.Success -> {
                    val usdcAmount = validationResult.data
                    updateUiState { copy(usdcAmount = usdcAmount) }

                    val state = _uiState.value
                    state.selectedCurrency?.let { currency ->
                        when (val conversionResult = calculateConversionUseCase(
                            usdcAmount,
                            currency.exchangeRate
                        )) {
                            is Result.Success -> {
                                updateUiState {
                                    copy(
                                        otherAmount = conversionResult.data,
                                        otherAmountInput = validateAmountUseCase.formatAmount(
                                            conversionResult.data
                                        ),
                                        errorMessage = null
                                    )
                                }
                            }

                            is Result.Failure -> {
                                updateUiState {
                                    copy(errorMessage = "Invalid amount")
                                }
                            }

                            Result.Loading -> {}
                        }
                    }
                }

                is Result.Failure -> {
                    updateUiState {
                        copy(
                            errorMessage = "Invalid amount",
                            otherAmount = 0.0,
                            otherAmountInput = ""
                        )
                    }
                }

                Result.Loading -> {}
            }
        }
    }

    fun onOtherAmountChanged(amount: String) {
        updateUiState { copy(otherAmountInput = amount) }

        viewModelScope.launch {
            delay(300)

            when (val validationResult = validateAmountUseCase(amount)) {
                is Result.Success -> {
                    val otherAmount = validationResult.data
                    updateUiState { copy(otherAmount = otherAmount) }

                    val state = _uiState.value
                    state.selectedCurrency?.let { currency ->
                        when (val conversionResult = calculateConversionUseCase.reverseConversion(
                            otherAmount,
                            currency.exchangeRate
                        )) {
                            is Result.Success -> {
                                updateUiState {
                                    copy(
                                        usdcAmount = conversionResult.data,
                                        usdcAmountInput = validateAmountUseCase.formatAmount(
                                            conversionResult.data
                                        ),
                                        errorMessage = null
                                    )
                                }
                            }

                            is Result.Failure -> {
                                updateUiState {
                                    copy(errorMessage = "Invalid amount")
                                }
                            }

                            Result.Loading -> {}
                        }
                    }
                }

                is Result.Failure -> {
                    updateUiState {
                        copy(
                            errorMessage = "Invalid amount",
                            usdcAmount = 0.0,
                            usdcAmountInput = ""
                        )
                    }
                }

                Result.Loading -> {}
            }
        }
    }

    fun selectCurrency(currency: Currency) {
        updateUiState { copy(selectedCurrency = currency) }

        // Recalculate with new currency rate
        val state = _uiState.value
        when (val result = calculateConversionUseCase(
            state.usdcAmount,
            currency.exchangeRate
        )) {
            is Result.Success -> {
                updateUiState {
                    copy(
                        otherAmount = result.data,
                        otherAmountInput = validateAmountUseCase.formatAmount(result.data),
                        errorMessage = null
                    )
                }
            }

            is Result.Failure -> {
                updateUiState { copy(errorMessage = "Error calculating conversion") }
            }

            Result.Loading -> {}
        }
    }

    fun swapCurrencies() {
        val state = _uiState.value

        updateUiState {
            copy(
                usdcAmount = state.otherAmount,
                usdcAmountInput = state.otherAmountInput,
                otherAmount = state.usdcAmount,
                otherAmountInput = state.usdcAmountInput
            )
        }
    }

    fun clearError() {
        updateUiState { copy(errorMessage = null) }
    }

    fun retry() {
        loadAvailableCurrencies()
    }

    private fun updateUiState(block: ExchangeCalculatorUiState.() -> ExchangeCalculatorUiState) {
        _uiState.value = _uiState.value.block()
    }
}

data class ExchangeCalculatorUiState(
    val availableCurrencies: List<Currency> = emptyList(),
    val selectedCurrency: Currency? = null,
    val usdcAmount: Double = 0.0,
    val usdcAmountInput: String = "",
    val otherAmount: Double = 0.0,
    val otherAmountInput: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isNetworkAvailable: Boolean = true
) {
    val selectedCurrencyName: String
        get() = selectedCurrency?.code ?: "Select Currency"

    val isEnabled: Boolean
        get() = !isLoading

    val isValidForCalculation: Boolean
        get() = selectedCurrency != null && isNetworkAvailable
}
