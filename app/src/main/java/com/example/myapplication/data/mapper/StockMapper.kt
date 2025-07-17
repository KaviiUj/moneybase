package com.example.myapplication.data.mapper

import com.example.myapplication.data.remote.dto.stock.ResultDto
import com.example.myapplication.domain.stockSummary.model.StockResult

fun ResultDto.toStockResult(): StockResult {
    return StockResult(
        quoteSummary = quoteSummary,
        language = language,
        region = region,
        quoteSourceName = quoteSourceName,
        exchangeTimezoneName = exchangeTimezoneName,
        shortName = shortName,
        fullExchangeName = fullExchangeName,
        exchange = exchange,
        market = market,
    )
}