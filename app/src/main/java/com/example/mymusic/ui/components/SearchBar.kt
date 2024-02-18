package com.example.mymusic.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex


@Composable
fun SearchBar(
    placeholderText: String,
    onClose: () -> Unit
) {
    val density = LocalDensity.current

    var text by remember { mutableStateOf(TextFieldValue("")) }
    var searchBarVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
//    val searchBarHeightInPx = with(density) { height.toPx() + statusBarHeight.toPx() }

//    val searchBarAnim = animateFloatAsState(
//        targetValue = when(state.value) {
//            true -> 1.0f
//            else -> 0.0f
//        },
//        animationSpec = tween(durationMillis = 300, easing = EaseInOut),
//        label = "searchbar anim",
//        finishedListener = { value ->
//            Log.d("SearchBar", "finishedListener value $value")
//        }
//    )
//
//    Log.d("SearchBar", "state.value ${state.value}")


    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(2f)
            .focusRequester(focusRequester)
            .background(MaterialTheme.colorScheme.background),
        value = text,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent//MaterialTheme.colorScheme.background
        ),
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 18.sp
        ),
        placeholder = {
            Text(
                //                            modifier = Modifier.align(Alignment.Center),
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
        onValueChange = { value ->
            text = value
            //                        onValueChange(value.text)
            //                        viewModel.searchQuery.value = text
        },
        trailingIcon = {
            IconButton(
                onClick = {
                    text = TextFieldValue("")
                    searchBarVisible = false
                    keyboardController?.hide()
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

//    LaunchedEffect(Unit) {
//        if (state.value) {
//            focusRequester.requestFocus()
//        }
//    }
}
