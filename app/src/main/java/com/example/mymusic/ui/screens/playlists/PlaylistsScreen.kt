package com.example.mymusic.ui.screens.playlists

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mymusic.ui.screens.playlists.ui.theme.MyMusicTheme
import com.example.mymusic.ui.viewmodels.HomeViewModel

@Composable
fun PlaylistsScreen(
    onNavigation: () -> Unit,
    viewModel: HomeViewModel
) {

    MyMusicTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Greeting6("Android")
        }
    }
}

@Composable
fun Greeting6(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview6() {
    MyMusicTheme {
        Greeting6("Android")
    }
}