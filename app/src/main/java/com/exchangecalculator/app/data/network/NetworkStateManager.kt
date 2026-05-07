package com.exchangecalculator.app.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.ContextCompat
import com.exchangecalculator.app.domain.network.NetworkMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkStateManager @Inject constructor(
    private val context: Context
) : NetworkMonitor {

    private val _isConnected = MutableStateFlow(checkConnection())
    override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val connectivityManager =
        ContextCompat.getSystemService(context, ConnectivityManager::class.java)

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isConnected.value = true
        }

        override fun onLost(network: Network) {
            _isConnected.value = checkConnection()
        }

        override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) {
            _isConnected.value = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
    }

    init {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager?.registerNetworkCallback(request, networkCallback)
    }

    private fun checkConnection(): Boolean {
        return try {
            val network = connectivityManager?.activeNetwork ?: return false
            val caps = connectivityManager.getNetworkCapabilities(network) ?: return false
            caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } catch (e: Exception) {
            false
        }
    }
}
