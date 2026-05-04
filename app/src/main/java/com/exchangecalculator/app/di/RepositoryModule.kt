package com.exchangecalculator.app.di

import com.exchangecalculator.app.data.remote.ExchangeRateApi
import com.exchangecalculator.app.data.repository.CurrencyRepositoryImpl
import com.exchangecalculator.app.data.repository.HardcodedCurrencyDataSource
import com.exchangecalculator.app.data.repository.ICurrencyRepository
import com.exchangecalculator.app.data.repository.NetworkCurrencyDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideHardcodedCurrencyDataSource(): HardcodedCurrencyDataSource {
        return HardcodedCurrencyDataSource()
    }

    @Provides
    @Singleton
    fun provideNetworkCurrencyDataSource(
        exchangeRateApi: ExchangeRateApi
    ): NetworkCurrencyDataSource {
        return NetworkCurrencyDataSource(exchangeRateApi)
    }
    @Provides
    @Singleton
    fun provideCurrencyRepository(
        hardcodedDataSource: HardcodedCurrencyDataSource
    ): ICurrencyRepository {
        return CurrencyRepositoryImpl(dataSource = hardcodedDataSource)
    }
}
