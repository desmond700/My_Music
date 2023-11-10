package com.example.mymusic.ui.screens.artists

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mymusic.ui.screens.artists.ui.theme.MyMusicTheme
import com.example.mymusic.ui.viewmodels.HomeViewModel

@Composable
fun ArtistsScreen(
    onNavigation: () -> Unit,
    viewModel: HomeViewModel
) {
    MyMusicTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Greeting4("Android")
        }
    }
}

@Composable
fun Greeting4(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    MyMusicTheme {
        Greeting4("Android")
    }
}