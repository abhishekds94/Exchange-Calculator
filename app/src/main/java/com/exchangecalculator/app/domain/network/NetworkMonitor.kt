package com.exchangecalculator.app.domain.network

import kotlinx.coroutines.flow.StateFlow

interface NetworkMonitor {
    val isConnected: StateFlow<Boolean>
}
