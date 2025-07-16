package com.example.myapplication.domain.marketSummary

import com.example.myapplication.domain.marketSummary.model.Result
import com.example.myapplication.util.Resource
import kotlinx.coroutines.flow.Flow

interface MarketSummaryRepo {
    suspend fun getMarketSummary(): List<Result>
}