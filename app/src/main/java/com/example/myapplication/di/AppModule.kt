package com.example.myapplication.di

import com.example.myapplication.data.remote.ApiService
import com.example.myapplication.data.repository.MarketSummaryRepoImpl
import com.example.myapplication.domain.marketSummary.MarketSummaryRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://yh-finance.p.rapidapi.com/"

    @Provides
    @Singleton
    fun provideStockApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideStockRepository(api: ApiService): MarketSummaryRepo {
        return MarketSummaryRepoImpl(api)
    }
}