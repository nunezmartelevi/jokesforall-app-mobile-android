package com.levi.jokesforall.ui.views.frames

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.levi.jokesforall.R
import com.levi.jokesforall.ui.theme.Wood
import com.levi.jokesforall.util.PIXEL_1_VIEW_PORT
import com.levi.jokesforall.util.PIXEL_4_VIEW_PORT
import com.levi.jokesforall.util.isHighWidthViewPort

@Composable
fun ConsoleFrame(
    modifier: Modifier = Modifier,
    maxWidth: Dp,
    maxHeight: Dp,
    onAButtonPress: () -> Unit = {},
    onBButtonPress: () -> Unit = {},
    onSoundButtonPress: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Wood)
    ) {
        // sets buttons size based on screen size
        val screenSize = maxWidth + maxHeight
        val aButtonSize = (screenSize.value * 0.07f).dp
        val bButtonSize = (screenSize.value * 0.05f).dp
        val soundButtonSize = (screenSize.value * 0.05f).dp

        // sets buttons offset based on screen max width and max height
        val baseXOffset = if (isHighWidthViewPort(maxWidth, maxHeight)) 0.02 else 0.0
        val baseYOffset = if (isHighWidthViewPort(maxWidth, maxHeight)) 0.01 else 0.0

        val aButtonOffset = Pair(
            (-maxWidth.value * (0.06 + baseXOffset)).dp,
            (-maxHeight.value * (0.1 + baseYOffset)).dp
        )
        val bButtonOffset = Pair(
            (-maxWidth.value * (0.3 + baseXOffset)).dp,
            (-maxHeight.value * (0.18 + baseYOffset)).dp
        )
        val soundButtonOffset = Pair(
            (maxWidth.value * (0.06 + baseXOffset)).dp,
            (-maxHeight.value * (0.21 + baseYOffset)).dp
        )

        // Wood frame
        Image(
            modifier = Modifier.matchParentSize(),
            painter = painterResource(id = R.drawable.ic_game_console_frame),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        // A button
        AnimatedImageButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = aButtonOffset.first, y = aButtonOffset.second)
                .shadow(
                    elevation = 10.dp,
                    shape = CircleShape,
                    clip = true,
                    spotColor = Color.Black.copy(alpha = 1f)
                ),
            size = aButtonSize,
            imageRes = R.drawable.ic_button_a,
            onClick = onAButtonPress
        )

        // B button
        AnimatedImageButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = bButtonOffset.first, y = bButtonOffset.second)
                .shadow(
                    elevation = 10.dp,
                    shape = CircleShape,
                    clip = true,
                    ambientColor = Color.Black.copy(alpha = 0.8f),
                    spotColor = Color.Black.copy(alpha = 1f)
                ),
            size = bButtonSize,
            imageRes = R.drawable.ic_button_b,
            onClick = onBButtonPress
        )

        // Sound button
        AnimatedImageButton(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = soundButtonOffset.first, y = soundButtonOffset.second),
            size = soundButtonSize,
            imageRes = R.drawable.ic_button_music,
            onClick = onSoundButtonPress
        )

    }
}

@Preview(device = PIXEL_4)
@Composable
fun WoodFramePreview() {
    ConsoleFrame(maxWidth = PIXEL_4_VIEW_PORT.first, maxHeight = PIXEL_4_VIEW_PORT.second)
}

@Preview(device = PIXEL)
@Composable
fun WoodFramePreview2() {
    ConsoleFrame(maxWidth = PIXEL_1_VIEW_PORT.first, maxHeight = PIXEL_1_VIEW_PORT.second)
}
