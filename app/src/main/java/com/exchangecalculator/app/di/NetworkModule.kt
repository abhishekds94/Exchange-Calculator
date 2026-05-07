package com.exchangecalculator.app.di

import android.content.Context
import com.exchangecalculator.app.BuildConfig
import com.exchangecalculator.app.data.network.NetworkInterceptor
import com.exchangecalculator.app.data.network.NetworkStateManager
import com.exchangecalculator.app.data.remote.ExchangeRateApi
import com.exchangecalculator.app.domain.network.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideNetworkStateManager(
        @ApplicationContext context: Context
    ): NetworkStateManager = NetworkStateManager(context)

    @Provides
    @Singleton
    fun provideNetworkMonitor(
        networkStateManager: NetworkStateManager
    ): NetworkMonitor = networkStateManager

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.ENABLE_LOGGING)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        networkInterceptor: NetworkInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(networkInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideExchangeRateApi(retrofit: Retrofit): ExchangeRateApi =
        retrofit.create(ExchangeRateApi::class.java)
}
