package com.example.myapplication.presentation.market

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.presentation.market.components.MarketItem
import com.example.myapplication.presentation.market.components.SearchBar
import com.google.gson.Gson

@Composable
fun SummaryScreen(
    viewModel: MarketViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 15.dp)
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
        ){
            Text(
                text = "Stock Market",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            SearchBar(
                modifier = Modifier.padding(top = 16.dp),
                onSearchQueryChanged = { query ->
                    println("Search query: $query")
                },
                placeholder = "Search markets..."
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (state.result.isNotEmpty()) {
                Log.i("viewDataList", "data: ${Gson().toJson(state.result)}")
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.result) { result ->
                        MarketItem(result = result)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
            if (state.error != null) {
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
            }
            if (state.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}