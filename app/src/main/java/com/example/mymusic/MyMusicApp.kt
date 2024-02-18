package com.example.mymusic

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mymusic.ui.screens.home.HomeScreen
import com.example.mymusic.ui.screens.menu.MenuScreen
import com.example.mymusic.ui.screens.settings.SettingsScreen
import com.example.mymusic.utils.BottomBarItemType
import com.example.mymusic.utils.bottomNarItems

enum class Destination {
    Menu,
    Home,
    Settings
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyMusicApp() {
    val progress by remember {
        mutableFloatStateOf(0f)
    }

    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    val screenWidth = with(density) { configuration.screenWidthDp.dp.toPx() }
    val menuWidth = with(density) { configuration.screenWidthDp.dp.toPx() - 80f.dp.toPx() }
//    val subScreenWidth = screenWidth - 140
    Log.d("MyMusicApp", "screenWidth $screenWidth")
    var selected by remember { mutableStateOf(BottomBarItemType.Home) }

    val coroutineScope = rememberCoroutineScope()

    val anchors = DraggableAnchors {
        Destination.Menu at menuWidth
        Destination.Home at 0f
        Destination.Settings at -screenWidth
    }

    val anchoredDraggableState = remember {
        AnchoredDraggableState(
            initialValue = Destination.Home,
            anchors = anchors,
            positionalThreshold = { distance: Float -> distance * 0.5f },
            animationSpec = spring(),
            velocityThreshold = { with(density) { 80.dp.toPx() } },
            confirmValueChange = { value ->
//                selected = value
                true
            }
        )
    }

    Log.d("InstagramApp", "anchoredDraggableState requireOffset ${anchoredDraggableState.requireOffset()}")


//    Log.d("MyMusicApp", "fraction width ${menuWidth / screenWidth}")

    // A surface container using the 'background' color from the theme
    Scaffold {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding()
        ) {
//            val (screenContainer, bottomBar) = createRefs()
            Box(
                modifier = Modifier
                    .fillMaxSize()
//                    .background(Color.Magenta)
                    .border(1.dp, Color.Green)
                    .anchoredDraggable(
                        state = anchoredDraggableState,
                        orientation = Orientation.Horizontal
                    )
            ) {

                MenuScreen(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .border(1.dp, Color.Red)
                        .graphicsLayer {
                            this.translationX = menuWidth + anchoredDraggableState.requireOffset()
                        }
                )



                HomeScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            this.translationX = anchoredDraggableState.requireOffset()
                        }
                )
                Log.d("MyMusicApp", "progress $progress")

                SettingsScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            this.translationX = screenWidth + anchoredDraggableState.requireOffset()
                        }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selected: BottomBarItemType,
    modifier: Modifier = Modifier,
    progress: Float,
    click: (type: BottomBarItemType) -> Unit) {

    BottomAppBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background
    ) {

        bottomNarItems.forEach {item ->
            NavigationBarItem(
                modifier = Modifier.padding(vertical = 5.dp),
                selected = selected == item.type,
                onClick = {
                    click(item.type)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.iconDescription
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun BottomNavigationBarPreview() {
    Box {
        BottomNavigationBar(
            selected = BottomBarItemType.Home,
            progress = 1f,
            click = {}
        )
    }
}

@Preview
@Composable
fun MyMusicAppPreview() {
    MyMusicApp()
}