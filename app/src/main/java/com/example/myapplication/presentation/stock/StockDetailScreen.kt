package com.example.myapplication.presentation.stock

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.StockDetailScreenBinding
import com.example.myapplication.domain.stockSummary.model.StockResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class StockDetailScreen: ComponentActivity() {

    private lateinit var binding: StockDetailScreenBinding
    private val viewModel: StockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = StockDetailScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(45, systemBars.top, 45, systemBars.bottom)
            insets
        }
        val symbol = intent.getStringExtra("SYMBOL") ?: "Unknown"
        Log.i("clickItemValue", "symbol: $symbol")
        fetchStockSummary(symbol)
        binding.goBack.setOnClickListener { finish() }
    }

    private fun fetchStockSummary(symbol: String) {
        viewModel.getStockSummary(symbol)
        viewModel.state.onEach { state ->
            when {
                state.isLoading -> {
                    binding.isLoading.visibility = View.VISIBLE
                    binding.dataList.visibility = View.GONE
                    binding.errMessage.visibility = View.GONE
                }
                state.error != null -> {
                    binding.isLoading.visibility = View.GONE
                    binding.dataList.visibility = View.GONE
                    binding.errMessage.visibility = View.VISIBLE
                    binding.errMessage.text = state.error ?: "Unexpected error occurred!"
                }
                state.result.isNotEmpty() -> {
                    state.result.firstOrNull()?.let { result ->
                        binding.isLoading.visibility = View.GONE
                        binding.errMessage.visibility = View.GONE
                        binding.dataList.visibility = View.VISIBLE
                        Log.i("DetailsList", "data: ${Gson().toJson(result)}")
                        setDataToUi(result)
                    }
                }
            }
        }.launchIn(lifecycleScope)
    }

    @SuppressLint("SetTextI18n")
    private fun setDataToUi(stockResult: StockResult) {
        binding.apply {
            quoteSourceName.text = "${stockResult.quoteSourceName} - ${stockResult.region}"
            includeCards.apply {
                exchangeTimezoneName.text = stockResult.exchangeTimezoneName
                shortName.text = stockResult.shortName
                exchange.text = stockResult.exchange
                market.text = stockResult.market
                fullExchangeName.text = stockResult.fullExchangeName
                fiftyDayAverage.text = stockResult.quoteSummary.summaryDetail.fiftyDayAverage.toString()
                twoHundredDayAverage.text = stockResult.quoteSummary.summaryDetail.twoHundredDayAverage.toString()
                currency.text = stockResult.quoteSummary.summaryDetail.currency
                maxAge.text = stockResult.quoteSummary.summaryDetail.maxAge.toString()
                priceHint.text = stockResult.quoteSummary.summaryDetail.priceHint.toString()
                previousClose.text = stockResult.quoteSummary.summaryDetail.previousClose.toString()
                open.text = stockResult.quoteSummary.summaryDetail.open.toString()
                dayLow.text = stockResult.quoteSummary.summaryDetail.dayLow.toString()
                dayHigh.text = stockResult.quoteSummary.summaryDetail.dayHigh.toString()
                expireDate.text = stockResult.quoteSummary.summaryDetail.expireDate.toString()
                openInterest.text = stockResult.quoteSummary.summaryDetail.openInterest.toString()
                fiftyTwoWeekLow.text = stockResult.quoteSummary.summaryDetail.fiftyTwoWeekLow.toString()
                fiftyTwoWeekHigh.text = stockResult.quoteSummary.summaryDetail.fiftyTwoWeekHigh.toString()
                regularMarketPreviousClose.text = stockResult.quoteSummary.summaryDetail.regularMarketPreviousClose.toString()
                regularMarketOpen.text = stockResult.quoteSummary.summaryDetail.regularMarketOpen.toString()
                regularMarketDayLow.text = stockResult.quoteSummary.summaryDetail.regularMarketDayLow.toString()
                regularMarketDayHigh.text = stockResult.quoteSummary.summaryDetail.regularMarketDayHigh.toString()
                volume.text = stockResult.quoteSummary.summaryDetail.volume.toString()
                regularMarketVolume.text = stockResult.quoteSummary.summaryDetail.regularMarketVolume.toString()
                averageVolume.text = stockResult.quoteSummary.summaryDetail.averageVolume.toString()
                averageVolume10days.text = stockResult.quoteSummary.summaryDetail.averageVolume10days.toString()
                bid.text = stockResult.quoteSummary.summaryDetail.bid.toString()
                bidSize.text = stockResult.quoteSummary.summaryDetail.bidSize.toString()
                bidSize.text = stockResult.quoteSummary.summaryDetail.bidSize.toString()
            }
        }
    }
}