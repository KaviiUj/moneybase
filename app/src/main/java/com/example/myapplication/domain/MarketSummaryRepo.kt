package com.example.myapplication.domain

import com.example.myapplication.domain.marketSummary.model.Result
import com.example.myapplication.domain.stockSummary.model.StockResult

interface MarketSummaryRepo {
    suspend fun getMarketSummary(): List<Result>
    suspend fun getStockSummary(symbol: String): List<StockResult>
}