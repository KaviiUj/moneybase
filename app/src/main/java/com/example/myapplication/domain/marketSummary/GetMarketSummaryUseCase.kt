package com.example.myapplication.domain.marketSummary

import com.example.myapplication.domain.marketSummary.model.Result
import com.example.myapplication.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetMarketSummaryUseCase @Inject constructor(
    private val marketSummaryRepo: MarketSummaryRepo
) {
    operator fun invoke(): Flow<Resource<List<Result>>> = flow {
        try {
            emit(Resource.Loading())
            val stocks = marketSummaryRepo.getMarketSummary()
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