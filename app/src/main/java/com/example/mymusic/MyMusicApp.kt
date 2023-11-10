package com.example.mymusic

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymusic.data.AppContainerImpl
import com.example.mymusic.ui.Search
import com.example.mymusic.ui.screens.home.HomeScreen
import com.example.mymusic.ui.viewmodels.HomeViewModel
import com.example.mymusic.utils.MainRoutes

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyMusicApp() {
//    val systemUiController = rememberSystemUiController()
//
//    SideEffect {
//        systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = true)
//    }
    val appContainer = AppContainerImpl()
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.provideFactory(appContainer.fileRepository)
    )

    // A surface container using the 'background' color from the theme
    Surface() {
        Scaffold(
            modifier = Modifier.navigationBarsPadding(),
            content = {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = MainRoutes.Home.route) {
                    composable(MainRoutes.Home.route) {
                        HomeScreen(homeViewModel = homeViewModel)
                    }
                    composable(MainRoutes.Search.route) {
                        Search()
                    }
                }
            },
            bottomBar = {
                BottomNavigationBar()
            }
        )
    }
}

@Composable
fun BottomNavigationBar() {
    BottomAppBar(
        modifier = Modifier.height(60. dp),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = {  },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = null
                )
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar();
}