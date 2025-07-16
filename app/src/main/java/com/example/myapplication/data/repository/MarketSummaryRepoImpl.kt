package com.example.myapplication.data.repository

import com.example.myapplication.data.mapper.toResult
import com.example.myapplication.data.remote.ApiService
import com.example.myapplication.domain.marketSummary.MarketSummaryRepo
import com.example.myapplication.domain.marketSummary.model.Result
import javax.inject.Inject

class MarketSummaryRepoImpl @Inject constructor(
    private val apiService: ApiService
): MarketSummaryRepo {
    override suspend fun getMarketSummary(): List<Result> {
        val response = apiService.getMarketSummary(apiKey = "YOUR_API_KEY")
        return response.marketSummaryAndSparkResponse.resultDto.map { it.toResult() }
    }
}