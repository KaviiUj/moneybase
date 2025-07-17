package com.example.myapplication.data.remote.dto.market

data class Spark(
    val chartPreviousClose: Double,
    val close: List<Double>,
    val dataGranularity: Int,
    val end: Int,
    val previousClose: Double,
    val start: Int,
    val symbol: String,
    val timestamp: List<Int>
)