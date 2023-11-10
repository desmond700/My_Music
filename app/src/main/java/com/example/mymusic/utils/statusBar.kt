package com.example.mymusic.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.example.mymusic.ui.theme.Typography
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun BarColorsTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    if(darkTheme){
        systemUiController.setSystemBarsColor(color = colorScheme.background,darkIcons = false)
        systemUiController.setNavigationBarColor( color = colorScheme.background,darkIcons = false)
    }else{
        systemUiController.setSystemBarsColor(color = colorScheme.background,darkIcons = true)
        systemUiController.setNavigationBarColor( color = colorScheme.background,darkIcons = true)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun PlatformColors(statusBarColor: Color, navBarColor: Color = Color.DarkGray){
    val sysUiController = rememberSystemUiController()
    SideEffect {
        sysUiController.setSystemBarsColor(color = statusBarColor)
        sysUiController.setNavigationBarColor(color = navBarColor)
    }
}