package com.example.myapplication.presentation.market

import com.example.myapplication.domain.marketSummary.model.Result

data class MarketState(
    val result: List<Result> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
