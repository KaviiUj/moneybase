package com.example.myapplication.data.remote.dto.stock

data class QuoteResponse(
    val error: Any,
    val result: List<ResultDto>
)