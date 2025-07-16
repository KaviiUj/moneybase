package com.example.myapplication.domain.marketSummary

import com.example.myapplication.domain.marketSummary.model.Result

interface MarketSummaryRepo {
    suspend fun getMarketSummary(): List<Result>
}