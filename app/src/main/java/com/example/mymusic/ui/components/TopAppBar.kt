package com.example.mymusic.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    label: String,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    placeholderText: String,
    onValueChange: (text: String) -> Unit,
    onNavigation: () -> Unit,
    onClose: () -> Unit
    ) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var searchBarVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth()
            .border(1.dp, Color.Green, RoundedCornerShape(1.dp)),
    ) {
        CenterAlignedTopAppBar(
            modifier = Modifier
                .height(60.dp)
                .zIndex(1f),
            colors = colors,
            navigationIcon = {
                IconButton(
                    modifier = Modifier.align(alignment = Alignment.Center),
                    onClick = onNavigation
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            },
            title = {
                Text(
                    modifier = Modifier.align(alignment = Alignment.Center),
                    text = label,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            },
            actions = {
                Box(
                    modifier = Modifier.fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            searchBarVisible = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                }
            }
        )
        if (searchBarVisible) {
            TextField(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(2f)
                    .focusRequester(focusRequester),
                value = text,
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp
                ),
                placeholder = {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = placeholderText,
                        style = TextStyle(
                            color = Color.LightGray,
                            fontSize = 18.sp
                        )
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                onValueChange = {value ->
                    text = value
                    onValueChange(value.text)
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            text = TextFieldValue("")
                            searchBarVisible = false
                            onClose()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                }
            )
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }

}