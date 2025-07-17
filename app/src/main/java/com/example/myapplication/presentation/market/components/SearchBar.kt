package com.example.myapplication.presentation.market.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearchQueryChanged: (String) -> Unit = {},
    placeholder: String = "Search..."
) {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = searchQuery.value,
            onValueChange = {
                searchQuery.value = it
                onSearchQueryChanged(it.text)
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                color = Color.Black
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    // Leading Search Icon
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(24.dp),
                        tint = Color.Black.copy(alpha = 0.6f)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp, end = 32.dp)
                    ) {
                        if (searchQuery.value.text.isEmpty()) {
                            androidx.compose.material3.Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black.copy(alpha = 0.4f),
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }

                    if (searchQuery.value.text.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                searchQuery.value = TextFieldValue("")
                                onSearchQueryChanged("")
                            },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Search",
                                tint = Color.Black.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        )
    }
}