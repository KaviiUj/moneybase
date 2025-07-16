package com.example.myapplication.presentation.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.marketSummary.GetMarketSummaryUseCase
import com.example.myapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val getMarketSummaryUseCase: GetMarketSummaryUseCase
): ViewModel() {

    private val _state = MutableStateFlow(MarketState())
    val state = _state.asStateFlow()

    init {
        getMarketSummary()
    }

    private fun getMarketSummary() {
        getMarketSummaryUseCase().onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _state.value = MarketState(isLoading = true)
                }
                is Resource.Success -> {
                    _state.value = MarketState(result = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _state.value = MarketState(error = result.message ?: "An unexpected error occurred")
                }
            }
        }.launchIn(viewModelScope)
    }
}