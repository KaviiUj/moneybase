package com.example.myapplication.presentation.stock

import com.example.myapplication.domain.stockSummary.model.StockResult

data class StockState(
    val result: List<StockResult> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
