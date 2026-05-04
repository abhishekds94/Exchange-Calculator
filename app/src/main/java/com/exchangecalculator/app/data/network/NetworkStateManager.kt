package com.exchangecalculator.app.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NetworkStateManager(private val context: Context) {

    private val _isConnected = MutableStateFlow(isNetworkAvailable())
    val isConnected: Flow<Boolean> = _isConnected.asStateFlow()

    init {
        updateNetworkState()
    }

    fun isNetworkAvailable(): Boolean {
        return try {
            val connectivityManager = ContextCompat.getSystemService(
                context,
                ConnectivityManager::class.java
            ) ?: return false

            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun updateNetworkState() {
        _isConnected.value = isNetworkAvailable()
    }
}
