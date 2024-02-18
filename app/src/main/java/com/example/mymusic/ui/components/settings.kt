package com.example.mymusic.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListHeader(
    modifier: Modifier = Modifier,
    text: String = "Header",
    color: Color = Color.White) {
    Box(
        modifier = modifier
            .padding(horizontal = 18.dp)
            .height(40.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = TextStyle(
                color = color,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
fun ListItemDivider(modifier: Modifier = Modifier, color: Color = Color.White) {
    Box(modifier = Modifier.padding(vertical = 15.dp),) {
        HorizontalDivider(
            thickness = 1.dp,
            color = color
        )
    }
}