package com.example.mymusic.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMusicAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
    navigationIcon: @Composable () -> Unit = {},
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = colors,
        navigationIcon = navigationIcon,
        title = title,
        actions = actions
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MyMusicAppBarPreview() {
    MyMusicAppBar(
        title = {
            Text(
                text = "My Music",

            )
        },
        actions = {}
    )
}