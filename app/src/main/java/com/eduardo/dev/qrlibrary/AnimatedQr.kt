package com.eduardo.dev.qrlibrary

import android.graphics.Bitmap
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.delay

@Composable
fun AnimatedQr(content: String, logo: Bitmap) {
    var colorIndex by remember { mutableIntStateOf(0) }
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Magenta, Color.Cyan, Color.Yellow)

    val qrGenerator = remember { QrGenerator() }

    val animatedColor by animateColorAsState(
        targetValue = colors[colorIndex],
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )
    val qrBitmap = qrGenerator.generateQr(content = content, qrColor = animatedColor.toArgb(), logo = logo)

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            colorIndex = (colorIndex + 1) % colors.size
        }
    }

    Image(bitmap = qrBitmap.asImageBitmap(), contentDescription = null)
}