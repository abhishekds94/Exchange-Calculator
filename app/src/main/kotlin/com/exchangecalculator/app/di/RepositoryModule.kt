package com.exchangecalculator.app.di

import com.exchangecalculator.app.data.datasource.CurrencyDataSource
import com.exchangecalculator.app.data.datasource.NetworkCurrencyDataSource
import com.exchangecalculator.app.data.repository.CurrencyRepositoryImpl
import com.exchangecalculator.app.domain.repo.ICurrencyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.InternalSerializationApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @OptIn(InternalSerializationApi::class)
    @Provides
    @Singleton
    fun provideCurrencyDataSource(
        networkDataSource: NetworkCurrencyDataSource
    ): CurrencyDataSource = networkDataSource

    @Provides
    @Singleton
    fun provideCurrencyRepository(
        dataSource: CurrencyDataSource
    ): ICurrencyRepository = CurrencyRepositoryImpl(dataSource)
}
