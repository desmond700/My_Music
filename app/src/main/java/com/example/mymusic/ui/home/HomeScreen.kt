package com.example.mymusic.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.example.mymusic.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
                       Surface(modifier = Modifier.height(screenHeight - 120.dp)) {
                           YourActions()
                       }
        },
        sheetContainerColor = Color.White,
        sheetPeekHeight = 100.dp
    ) {

        // A surface container using the 'background' color from the theme
        Scaffold(
//            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = Color("#191B28".toColorInt()),
            topBar = { HomeTopAppBar(scope = scope, state = bottomSheetScaffoldState) }
        ) {val padding = it
            LazyColumn(
                contentPadding = padding,
            ) {
                item {
                    GridSections()
                }
                item {
                    YourActions()
                }
            }


        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(scope: CoroutineScope, state: BottomSheetScaffoldState) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color("#191B28".toColorInt()),
        ),
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    tint = Color.White,
                    contentDescription = null
                )
            }
        },
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
            )
        },
        actions = {
            IconButton(
                onClick = {
                    scope.launch {
                        Log.d("BOTTOM_SHEET", "bottomSheetState isVisible ${state.bottomSheetState.isVisible}")
                        Log.d("BOTTOM_SHEET", "bottomSheetState hasExpandedState ${state.bottomSheetState.hasExpandedState}")
//                        if (state.bottomSheetState.hasExpandedState) {
//                            state.bottomSheetState.hide()
//                        }
//                        else {
                            state.bottomSheetState.expand()
//                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = Color.White,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
fun HeaderLabel(text: String) {
    Text(
        modifier = Modifier.padding(bottom = 13.dp),
        text = text,
        style = TextStyle(
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GridSections() {
    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .fillMaxWidth(),
    ) {
        HeaderLabel(text = "Shortcuts")
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 2
        ) {
            SectionButton()
            SectionButton()
            SectionButton()
            SectionButton()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowScope.SectionButton() {
//    fun String.toColor() = Color(android.graphics.Color.parseColor(this))
    val brush = Brush.linearGradient(
        listOf(Color("#45475E".toColorInt()), Color("#353746".toColorInt())),
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 0f)
    )

    Button(
        modifier = Modifier
            .height(150.dp)
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(brush),
//        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        onClick = { /*TODO*/ }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(modifier = Modifier
                .height(42.dp)
                .width(42.dp),
                shape = RoundedCornerShape(50),
                color = Color.Gray
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null
                )
            }
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = "Library",
                color = Color.White
            )
        }
    }
}

@Composable
fun YourActions() {
    Column (
        modifier = Modifier.padding(top = 40.dp, start = 20.dp, end = 20.dp)
    ) {
        HeaderLabel(text = "Your actions")
        ActionButtonItem()
        ActionButtonItem()
        ActionButtonItem(showDivider = false)
    }
}

@Composable
fun ActionButtonItem(showDivider: Boolean = true) {
    Button(
        modifier = Modifier.padding(top = 5.dp, start = 0.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        onClick = { /*TODO*/ }
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Icon (
                imageVector = Icons.Default.Favorite,
                tint = Color.Gray,
                contentDescription = null
            )
            Column(modifier = Modifier.padding(start = 20.dp)) {
                Row(modifier = Modifier.padding(bottom = 10.dp)) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "History",
                        style = TextStyle(
                            color = Color.White
                        )
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        tint = Color.Gray,
                        contentDescription = null
                    )
                }
                if (showDivider)
                    HorizontalDivider(
                        color = Color.DarkGray,
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                    )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
    HomeTopAppBar(
        state = bottomSheetScaffoldState,
        scope = scope
    )
}

//@Preview(showBackground = true)
//@Composable
//fun GridSectionsPreview() {
//    GridSections()
//}

//@Preview(showBackground = true)
//@Composable
//fun ActionButtonItemPreview() {
//    ActionButtonItem()
//}
//
//@Preview(showBackground = true)
//@Composable
//fun YourActionsPreview() {
//    YourActions()
//}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}