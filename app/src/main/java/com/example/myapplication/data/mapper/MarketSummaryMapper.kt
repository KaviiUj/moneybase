package com.example.myapplication.data.mapper

import com.example.myapplication.data.remote.dto.market.ResultDto
import com.example.myapplication.domain.marketSummary.model.Result

fun ResultDto.toResult(): Result {
    return Result(
        exchange = exchange,
        exchangeDataDelayedBy = exchangeDataDelayedBy,
        exchangeTimezoneName = exchangeTimezoneName,
        exchangeTimezoneShortName = exchangeTimezoneShortName,
        fullExchangeName = fullExchangeName,
        gmtOffSetMilliseconds = gmtOffSetMilliseconds,
        language = language,
        market = market,
        marketState = marketState,
        priceHint = priceHint,
        quoteType = quoteType,
        region = region,
        regularMarketPrice = regularMarketPrice,
        shortName = shortName,
        sourceInterval = sourceInterval,
        symbol = symbol,
        triggerable = triggerable,
        regularMarketTime = regularMarketTime
    )
}