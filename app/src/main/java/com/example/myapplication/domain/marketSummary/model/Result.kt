package com.example.myapplication.domain.marketSummary.model

import com.example.myapplication.data.remote.dto.market.RegularMarketPrice
import com.example.myapplication.data.remote.dto.market.RegularMarketTime

data class Result(
    val exchange: String,
    val exchangeDataDelayedBy: Int,
    val exchangeTimezoneName: String,
    val exchangeTimezoneShortName: String,
    val fullExchangeName: String,
    val gmtOffSetMilliseconds: Int,
    val language: String,
    val market: String,
    val marketState: String,
    val priceHint: Int,
    val quoteType: String,
    val region: String,
    val regularMarketPrice: RegularMarketPrice,
    val regularMarketTime: RegularMarketTime,
    val shortName: String?,
    val sourceInterval: Int,
    val symbol: String,
    val triggerable: Boolean
)
