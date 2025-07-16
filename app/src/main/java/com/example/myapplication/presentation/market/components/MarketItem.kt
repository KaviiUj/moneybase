package com.example.myapplication.presentation.market.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.domain.marketSummary.model.Result
import com.example.myapplication.util.sampleMarketData
import kotlin.random.Random

@Composable
fun MarketItem(
    result: Result,
    onClick: () -> Unit = {}
) {
    val randomLightColor = Color(
        red = Random.nextFloat() * 0.3f + 0.7f,
        green = Random.nextFloat() * 0.3f + 0.7f,
        blue = Random.nextFloat() * 0.3f + 0.7f,
        alpha = 1f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = randomLightColor,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = result.fullExchangeName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = result.symbol,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = result.shortName ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 14.sp,
                    color = Color.Black.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Price",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 12.sp,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "${result.regularMarketPrice.raw}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Market State",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 12.sp,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                    Text(
                        text = result.marketState,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Exchange",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 12.sp,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                    Text(
                        text = result.exchange,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Time",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 12.sp,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                    Text(
                        text = result.regularMarketTime.fmt,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp
                    )
                }
            }

            Text(
                text = "Language: ${result.language}",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = Color.Black.copy(alpha = 0.5f),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.End)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MarketItemPreview() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(sampleMarketData) { result ->
            MarketItem(result = result)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}