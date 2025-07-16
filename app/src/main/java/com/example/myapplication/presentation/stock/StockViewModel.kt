package com.example.myapplication.presentation.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.GetMarketSummaryUseCase
import com.example.myapplication.domain.GetStockSummaryUseCase
import com.example.myapplication.presentation.market.MarketState
import com.example.myapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val getMarketSummaryUseCase: GetStockSummaryUseCase
): ViewModel() {

    private val _state = MutableStateFlow(StockState())
    val state = _state.asStateFlow()

    fun getStockSummary(symbol: String) {
        _state.value = _state.value.copy(isLoading = true, result = emptyList())
        getMarketSummaryUseCase(symbol).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        result = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}