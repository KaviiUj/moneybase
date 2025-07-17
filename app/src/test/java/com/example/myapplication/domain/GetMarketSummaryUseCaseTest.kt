package com.example.myapplication.domain

import com.example.myapplication.data.remote.dto.market.RegularMarketPrice
import com.example.myapplication.data.remote.dto.market.RegularMarketTime
import com.example.myapplication.domain.marketSummary.model.Result
import com.example.myapplication.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class GetMarketSummaryUseCaseTest {
    private lateinit var useCase: GetMarketSummaryUseCase
    private lateinit var mockMarketSummaryRepo: MarketSummaryRepo
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockMarketSummaryRepo = mockk()
        useCase = GetMarketSummaryUseCase(mockMarketSummaryRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createDummyResult() = Result(
        exchange = "NASDAQ",
        exchangeDataDelayedBy = 0,
        exchangeTimezoneName = "America/New_York",
        exchangeTimezoneShortName = "EDT",
        fullExchangeName = "NASDAQ",
        gmtOffSetMilliseconds = -14400000,
        language = "en-US",
        market = "us_market",
        marketState = "REGULAR",
        priceHint = 2,
        quoteType = "EQUITY",
        region = "US",
        regularMarketPrice = RegularMarketPrice(
            raw = 150.0,
            fmt = "150.00"
        ),
        shortName = "Apple Inc.",
        sourceInterval = 15,
        symbol = "AAPL",
        triggerable = true,
        regularMarketTime = RegularMarketTime(
            raw = 1633035600,
            fmt = "2021-10-01T16:00:00Z"
        )
    )

    @Test
    fun `invoke returns success with market summary data`() = runTest {
        val mockResults = listOf(createDummyResult())
        coEvery { mockMarketSummaryRepo.getMarketSummary() } returns mockResults

        val flow = useCase()
        val loading = flow.first()
        val success = flow.drop(1).first()

        assertTrue(loading is Resource.Loading)
        assertTrue(success is Resource.Success)
        assertEquals(mockResults, (success as Resource.Success).data)
    }

    @Test
    fun `invoke returns error when HttpException 401 occurs`() = runTest {
        val httpException = mockk<HttpException>()
        coEvery { httpException.code() } returns 401
        coEvery { httpException.localizedMessage } returns null
        coEvery { mockMarketSummaryRepo.getMarketSummary() } throws httpException

        val flow = useCase()
        val loading = flow.first()
        val error = flow.drop(1).first()

        assertTrue(loading is Resource.Loading)
        assertTrue(error is Resource.Error)
        assertEquals("Error 401: Unauthorized: Please check your API key.", (error as Resource.Error).message)
    }

    @Test
    fun `invoke returns error when HttpException 404 occurs`() = runTest {
        val httpException = mockk<HttpException>()
        coEvery { httpException.code() } returns 404
        coEvery { httpException.localizedMessage } returns null
        coEvery { mockMarketSummaryRepo.getMarketSummary() } throws httpException

        val flow = useCase()
        val loading = flow.first()
        val error = flow.drop(1).first()

        assertTrue(loading is Resource.Loading)
        assertTrue(error is Resource.Error)
        assertEquals("Error 404: Not Found: The requested resource was not found.", (error as Resource.Error).message)
    }

    @Test
    fun `invoke returns error when HttpException 500 occurs`() = runTest {
        val httpException = mockk<HttpException>()
        coEvery { httpException.code() } returns 500
        coEvery { httpException.localizedMessage } returns null
        coEvery { mockMarketSummaryRepo.getMarketSummary() } throws httpException

        val flow = useCase()
        val loading = flow.first()
        val error = flow.drop(1).first()

        assertTrue(loading is Resource.Loading)
        assertTrue(error is Resource.Error)
        assertEquals("Error 500: Server Error: Please try again later.", (error as Resource.Error).message)
    }

    @Test
    fun `invoke returns error when HttpException unknown occurs`() = runTest {
        val httpException = mockk<HttpException>()
        coEvery { httpException.code() } returns 503
        coEvery { httpException.localizedMessage } returns "Service Unavailable"
        coEvery { mockMarketSummaryRepo.getMarketSummary() } throws httpException

        val flow = useCase()
        val loading = flow.first()
        val error = flow.drop(1).first()

        assertTrue(loading is Resource.Loading)
        assertTrue(error is Resource.Error)
        assertEquals("Error 503: Service Unavailable", (error as Resource.Error).message)
    }

    @Test
    fun `invoke returns error when IOException occurs`() = runTest {
        coEvery { mockMarketSummaryRepo.getMarketSummary() } throws IOException("Network error")

        val flow = useCase()
        val loading = flow.first()
        val error = flow.drop(1).first()

        assertTrue(loading is Resource.Loading)
        assertTrue(error is Resource.Error)
        assertEquals("Couldn't reach server. Check your internet connection.", (error as Resource.Error).message)
    }

    @Test
    fun `invoke returns error when HttpException occurs with null localized message`() = runTest {
        val httpException = mockk<HttpException>()
        coEvery { httpException.code() } returns 400
        coEvery { httpException.localizedMessage } returns null
        coEvery { mockMarketSummaryRepo.getMarketSummary() } throws httpException

        val flow = useCase()
        val loading = flow.first()
        val error = flow.drop(1).first()

        assertTrue(loading is Resource.Loading)
        assertTrue(error is Resource.Error)
        assertEquals("Error 400: An unexpected HTTP error occurred", (error as Resource.Error).message)
    }
} 