package com.exchangecalculator.app.di

import com.exchangecalculator.app.data.repository.CurrencyRepositoryImpl
import com.exchangecalculator.app.data.repository.ICurrencyRepository
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
    fun provideCurrencyRepository(): ICurrencyRepository {
        return CurrencyRepositoryImpl()
    }
}
