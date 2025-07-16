package com.example.myapplication.domain

import com.example.myapplication.domain.stockSummary.model.StockResult
import com.example.myapplication.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetStockSummaryUseCase @Inject constructor(
    private val marketSummaryRepo: MarketSummaryRepo
) {
    operator fun invoke(symbol: String): Flow<Resource<List<StockResult>>> = flow {
        try {
            emit(Resource.Loading())
            val stocks = marketSummaryRepo.getStockSummary(symbol)
            emit(Resource.Success(stocks))
        } catch (e: HttpException) {
            val statusCode = e.code()
            val errorMessage = when (statusCode) {
                401 -> "Unauthorized: Please check your API key."
                404 -> "Not Found: The requested resource was not found."
                500 -> "Server Error: Please try again later."
                else -> e.localizedMessage ?: "An unexpected HTTP error occurred"
            }
            emit(Resource.Error("Error $statusCode: $errorMessage"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}