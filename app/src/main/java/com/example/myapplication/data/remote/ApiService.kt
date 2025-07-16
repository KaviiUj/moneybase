package com.example.myapplication.data.remote

import com.example.myapplication.data.remote.dto.market.MarketSummaryDto
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {

    @GET("market/v2/get-summary?region=US")
    suspend fun getMarketSummary(
        @Header("x-rapidapi-key") apiKey: String,
        @Header("x-rapidapi-host") apiHost: String = "yh-finance.p.rapidapi.com"
    ): MarketSummaryDto
}