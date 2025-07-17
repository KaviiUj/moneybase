package com.example.myapplication.domain

import com.example.myapplication.data.remote.dto.stock.QuoteSummary
import com.example.myapplication.data.remote.dto.stock.SummaryDetail
import com.example.myapplication.domain.stockSummary.model.StockResult
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
class GetStockSummaryUseCaseTest {
    private lateinit var useCase: GetStockSummaryUseCase
    private lateinit var mockMarketSummaryRepo: MarketSummaryRepo
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockMarketSummaryRepo = mockk()
        useCase = GetStockSummaryUseCase(mockMarketSummaryRepo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createDummyStockResult() = StockResult(
        quoteSummary = QuoteSummary(
            summaryDetail = SummaryDetail(
                algorithm = "SMA",
                ask = 150.5,
                askSize = 100,
                averageDailyVolume10Day = 1000000,
                averageVolume = 2000000,
                averageVolume10days = 1500000,
                bid = 150.25,
                bidSize = 200,
                coinMarketCapLink = "",
                currency = "USD",
                dayHigh = 152.75,
                dayLow = 149.80,
                expireDate = 1640995200,
                fiftyDayAverage = 145.30,
                fiftyTwoWeekHigh = 175.50,
                fiftyTwoWeekLow = 120.10,
                fromCurrency = "",
                lastMarket = "",
                maxAge = 1,
                open = 150.00,
                openInterest = 0,
                previousClose = 149.90,
                priceHint = 2,
                regularMarketDayHigh = 152.75,
                regularMarketDayLow = 149.80,
                regularMarketOpen = 150.00,
                regularMarketPreviousClose = 149.90,
                regularMarketVolume = 45000000,
                toCurrency = "",
                tradeable = true,
                twoHundredDayAverage = 140.20,
                volume = 4500
            )
        ),
        language = "en-US",
        region = "US",
        quoteSourceName = "Delayed Quote",
        exchangeTimezoneName = "America/New_York",
        shortName = "Apple Inc.",
        fullExchangeName = "NASDAQ",
        exchange = "NMS",
        market = "us_market"
    )

    @Test
    fun `invoke returns success with stock summary data`() = runTest {
        val symbol = "AAPL"
        val mockResults = listOf(createDummyStockResult())
        coEvery { mockMarketSummaryRepo.getStockSummary(symbol) } returns mockResults

        val flow = useCase(symbol)
        val loading = flow.first()
        val success = flow.drop(1).first()

        assertTrue(loading is Resource.Loading)
        assertTrue(success is Resource.Success)
        assertEquals(mockResults, (success as Resource.Success).data)
    }

    @Test
    fun `invoke returns error when HttpException 401 occurs`() = runTest {
        val symbol = "AAPL"
        val httpException = mockk<HttpException>()
        coEvery { httpException.code() } returns 401
        coEvery { httpException.localizedMessage } returns null
        coEvery { mockMarketSummaryRepo.getStockSummary(symbol) } throws httpException

        val flow = useCase(symbol)
        val loading = flow.first()
        val error = flow.drop(1).first()

        assertTrue(loading is Resource.Loading)
        assertTrue(error is Resource.Error)
        assertEquals("Error 401: Unauthorized: Please check your API key.", (error as Resource.Error).message)
    }

    @Test
    fun `invoke returns error when HttpException 404 occurs`() = runTest {
        val symbol = "INVALID"
        val httpException = mockk<HttpException>()
        coEvery { httpException.code() } returns 404
        coEvery { httpException.localizedMessage } returns null
        coEvery { mockMarketSummaryRepo.getStockSummary(symbol) } throws httpException

        val flow = useCase(symbol)
        val loading = flow.first()
        val error = flow.drop(1).first()

        assertTrue(loading is Resource.Loading)
        assertTrue(error is Resource.Error)
        assertEquals("Error 404: Not Found: The requested resource was not found.", (error as Resource.Error).message)
    }

    @Test
    fun `invoke returns error when HttpException 500 occurs`() = runTest {
        val symbol = "AAPL"
        val httpException = mockk<HttpException>()
        coEvery { httpException.code() } returns 500
        coEvery { httpException.localizedMessage } returns null
        coEvery { mockMarketSummaryRepo.getStockSummary(symbol) } throws httpException

        val flow = useCase(symbol)
        val loading = flow.first()
        val error = flow.drop(1).first()

        assertTrue(loading is Resource.Loading)
        assertTrue(error is Resource.Error)
        assertEquals("Error 500: Server Error: Please try again later.", (error as Resource.Error).message)
    }

    @Test
    fun `invoke returns error when HttpException unknown occurs`() = runTest {
        val symbol = "AAPL"
        val httpException = mockk<HttpException>()
        coEvery { httpException.code() } returns 503
        coEvery { httpException.localizedMessage } returns "Service Unavailable"
        coEvery { mockMarketSummaryRepo.getStockSummary(symbol) } throws httpException

        val flow = useCase(symbol)
        val loading = flow.first()
        val error = flow.drop(1).first()

        assertTrue(loading is Resource.Loading)
        assertTrue(error is Resource.Error)
        assertEquals("Error 503: Service Unavailable", (error as Resource.Error).message)
    }

    @Test
    fun `invoke returns error when IOException occurs`() = runTest {
        val symbol = "AAPL"
        coEvery { mockMarketSummaryRepo.getStockSummary(symbol) } throws IOException("Network error")

        val flow = useCase(symbol)
        val loading = flow.first()
        val error = flow.drop(1).first()

        assertTrue(loading is Resource.Loading)
        assertTrue(error is Resource.Error)
        assertEquals("Couldn't reach server. Check your internet connection.", (error as Resource.Error).message)
    }

    @Test
    fun `invoke returns error when HttpException occurs with null localized message`() = runTest {
        val symbol = "AAPL"
        val httpException = mockk<HttpException>()
        coEvery { httpException.code() } returns 400
        coEvery { httpException.localizedMessage } returns null
        coEvery { mockMarketSummaryRepo.getStockSummary(symbol) } throws httpException

        val flow = useCase(symbol)
        val loading = flow.first()
        val error = flow.drop(1).first()

        assertTrue(loading is Resource.Loading)
        assertTrue(error is Resource.Error)
        assertEquals("Error 400: An unexpected HTTP error occurred", (error as Resource.Error).message)
    }

    @Test
    fun `invoke returns success with empty list when no data found`() = runTest {
        val symbol = "EMPTY"
        val emptyResults = emptyList<StockResult>()
        coEvery { mockMarketSummaryRepo.getStockSummary(symbol) } returns emptyResults

        val flow = useCase(symbol)
        val loading = flow.first()
        val success = flow.drop(1).first()

        assertTrue(loading is Resource.Loading)
        assertTrue(success is Resource.Success)
        assertEquals(emptyResults, (success as Resource.Success).data)
        assertTrue(success.data!!.isEmpty())
    }

    @Test
    fun `invoke returns success with multiple stock results`() = runTest {
        val symbol = "MULTI"
        val mockResults = listOf(
            createDummyStockResult(),
            createDummyStockResult().copy(
                shortName = "Microsoft Corporation",
                exchange = "NMS"
            )
        )
        coEvery { mockMarketSummaryRepo.getStockSummary(symbol) } returns mockResults

        val flow = useCase(symbol)
        val loading = flow.first()
        val success = flow.drop(1).first()

        assertTrue(loading is Resource.Loading)
        assertTrue(success is Resource.Success)
        assertEquals(mockResults, (success as Resource.Success).data)
        assertEquals(2, success.data!!.size)
    }

    @Test
    fun `invoke handles different symbol formats`() = runTest {
        val symbols = listOf("AAPL", "AAPL.O", "AAPL-USD")
        
        symbols.forEach { symbol ->
            val mockResults = listOf(createDummyStockResult())
            coEvery { mockMarketSummaryRepo.getStockSummary(symbol) } returns mockResults

            val flow = useCase(symbol)
            val loading = flow.first()
            val success = flow.drop(1).first()

            assertTrue(loading is Resource.Loading)
            assertTrue(success is Resource.Success)
            assertEquals(mockResults, (success as Resource.Success).data)
        }
    }
} 