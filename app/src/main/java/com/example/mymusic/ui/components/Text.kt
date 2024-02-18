package com.example.mymusic.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

@Composable
fun TitleText(
    text: String,
    style: TextStyle = TextStyle()
) {
    Text(
        text = text,
        style = style.copy(
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun SubTitleText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = TextStyle()
) {
    Text(
        modifier = modifier,
        text = text,
        style = style.copy(
            color = Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}