package com.example.myapplication.domain.stockSummary.model

import com.example.myapplication.data.remote.dto.stock.QuoteSummary

data class StockResult(
    val quoteSummary: QuoteSummary,
    val language: String,
    val region: String,
    val quoteSourceName: String,
    val exchangeTimezoneName: String,
    val shortName: String,
    val fullExchangeName: String,
    val exchange: String,
    val market: String,
)
