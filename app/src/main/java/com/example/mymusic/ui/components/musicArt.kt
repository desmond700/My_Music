package com.example.mymusic.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.mymusic.R

@Composable
fun MusicArt(
    modifier: Modifier = Modifier,
    bitmap: Bitmap?,
    color: Color = Color.Transparent,
    shape: Shape = RoundedCornerShape(10.dp),
    shadowElevation: Dp = 15.dp
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color,
        shadowElevation = shadowElevation
    ) {
        if (bitmap != null) {
            Image(
                modifier = Modifier.fillMaxSize(),
                bitmap = bitmap.asImageBitmap(),
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )
        }
        else ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue)
                .padding(10.dp),
        ) {
            val (icon) = createRefs()
            Icon(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(icon) {
                        top.linkTo(parent.top, 20.dp)
                        bottom.linkTo(parent.bottom, 20.dp)
                        start.linkTo(parent.start, 20.dp)
                        end.linkTo(parent.end, 20.dp)
                    },
                painter = painterResource(id = R.drawable.music),
                contentDescription = null
            )
        }
    }
}