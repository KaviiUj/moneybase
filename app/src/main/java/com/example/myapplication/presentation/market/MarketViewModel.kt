package com.example.myapplication.presentation.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.marketSummary.GetMarketSummaryUseCase
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
class MarketViewModel @Inject constructor(
    private val getMarketSummaryUseCase: GetMarketSummaryUseCase
): ViewModel() {

    private val _state = MutableStateFlow(MarketState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                getMarketSummary()
                delay(8000)
            }
        }
    }

    private fun getMarketSummary() {
        _state.value = _state.value.copy(isLoading = true, result = emptyList())
        getMarketSummaryUseCase().onEach { result ->
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