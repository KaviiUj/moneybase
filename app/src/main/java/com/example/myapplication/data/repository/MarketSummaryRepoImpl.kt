package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.mapper.toResult
import com.example.myapplication.data.remote.ApiService
import com.example.myapplication.domain.marketSummary.MarketSummaryRepo
import com.example.myapplication.domain.marketSummary.model.Result
import com.google.gson.Gson
import javax.inject.Inject

class MarketSummaryRepoImpl @Inject constructor(
    private val apiService: ApiService
): MarketSummaryRepo {
    override suspend fun getMarketSummary(): List<Result> {
        val response = apiService.getMarketSummary(apiKey = "24aac0f661mshf31f9e65aa448c4p1f5848jsn68eff946f1f2")
        Log.i("viewDataList", "response: ${Gson().toJson(response)}")
        return response?.marketSummaryAndSparkResponse?.result?.map { it.toResult() } ?: emptyList()
    }
}