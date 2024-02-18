package com.example.mymusic.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

abstract class MenuButtonItem {}

data class MenuButtonIconItem(
    val imageVector: ImageVector,
    val color: Color = Color.DarkGray,
    val contentDescription: String? = null
)

data class MenuButtonListItem(
    val title: String,
    val icon: MenuButtonIconItem,
    val click: () -> Unit
): MenuButtonItem()

data class MenuButtonItemDivider(
    val divider: @Composable () -> Unit
) : MenuButtonItem()