package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.BuildConfig
import com.example.myapplication.data.mapper.toResult
import com.example.myapplication.data.mapper.toStockResult
import com.example.myapplication.data.remote.ApiService
import com.example.myapplication.domain.MarketSummaryRepo
import com.example.myapplication.domain.marketSummary.model.Result
import com.example.myapplication.domain.stockSummary.model.StockResult
import com.google.gson.Gson
import javax.inject.Inject

class MarketSummaryRepoImpl @Inject constructor(
    private val apiService: ApiService
): MarketSummaryRepo {
    override suspend fun getMarketSummary(): List<Result> {
        val response = apiService.getMarketSummary(apiKey = BuildConfig.API_KEY)
        Log.i("viewDataList", "response: ${Gson().toJson(response)}")
        return response?.marketSummaryAndSparkResponse?.result?.map { it.toResult() } ?: emptyList()
    }

    override suspend fun getStockSummary(symbol: String): List<StockResult> {
        val response = apiService.getStockSummary(
            apiKey = BuildConfig.API_KEY,
            symbols = symbol
        )
        Log.i("viewDataList", "response: ${Gson().toJson(response)}")
        return response?.quoteResponse?.result?.map { it.toStockResult() } ?: emptyList()
    }
}