package com.levi.jokesforall.ui.views.frames

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.levi.jokesforall.R
import com.levi.jokesforall.ui.views.MediaPlayerVolumeEffect
import com.levi.jokesforall.ui.views.rememberMediaPlayerState
import com.levi.jokesforall.util.PIXEL_1_VIEW_PORT
import com.levi.jokesforall.util.PIXEL_4_VIEW_PORT
import com.levi.jokesforall.util.isHighWidthViewPort

@Composable
fun WoodFrame(
    modifier: Modifier = Modifier,
    maxScreenWidth: Dp,
    maxScreenHeight: Dp,
    isSoundOn: Boolean,
    onAButtonPress: () -> Unit = {},
    onBButtonPress: () -> Unit = {},
    onSoundButtonPress: () -> Unit
) {
    val context = LocalContext.current
    val mediaPlayerState = rememberMediaPlayerState(context, R.raw.button_click_sound)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        lifecycleOwner.lifecycle.addObserver(mediaPlayerState)
        mediaPlayerState.prepare()
        onDispose {
            mediaPlayerState.clearMediaPlayer()
            lifecycleOwner.lifecycle.removeObserver(mediaPlayerState)
        }
    }

    MediaPlayerVolumeEffect(isSoundOn, mediaPlayerState)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // sets buttons size based on screen size
        val screenSize = maxScreenWidth + maxScreenHeight
        val aButtonSize = (screenSize.value * 0.07f).dp
        val bButtonSize = (screenSize.value * 0.05f).dp
        val soundButtonSize = (screenSize.value * 0.05f).dp

        // sets buttons offset based on screen max width and height
        val baseXOffset = if (isHighWidthViewPort(maxScreenWidth, maxScreenHeight)) 0.02 else 0.0
        val baseYOffset = if (isHighWidthViewPort(maxScreenWidth, maxScreenHeight)) 0.01 else 0.0
        val aButtonOffset = Pair(
            (-maxScreenWidth.value * (0.06 + baseXOffset)).dp,
            (-maxScreenHeight.value * (0.1 + baseYOffset)).dp
        )
        val bButtonOffset = Pair(
            (-maxScreenWidth.value * (0.3 + baseXOffset)).dp,
            (-maxScreenHeight.value * (0.18 + baseYOffset)).dp
        )
        val soundButtonOffset = Pair(
            (maxScreenWidth.value * (0.06 + baseXOffset)).dp,
            (-maxScreenHeight.value * (0.21 + baseYOffset)).dp
        )

        // Wood image
        Image(
            modifier = Modifier.matchParentSize(),
            painter = painterResource(id = R.drawable.ic_game_console_frame),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        // A button
        AnimatedButton(
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
            onClick = {
                onAButtonPress()
                mediaPlayerState.startOrResumePlayback()
            }
        )

        // B button
        AnimatedButton(
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
            onClick = {
                onBButtonPress()
                mediaPlayerState.startOrResumePlayback()
            }
        )

        // Sound button
        AnimatedButton(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = soundButtonOffset.first, y = soundButtonOffset.second),
            size = soundButtonSize,
            imageRes = R.drawable.ic_button_music,
            onClick = {
                onSoundButtonPress()
                mediaPlayerState.startOrResumePlayback()
            }
        )

    }
}

@Composable
private fun AnimatedButton(
    modifier: Modifier = Modifier,
    size: Dp,
    @DrawableRes imageRes: Int,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.8f else 1f,
        label = "buttonScaleAnimation"
    )

    Image(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null
            ),
        painter = painterResource(imageRes),
        contentDescription = null
    )
}

@Preview(device = PIXEL_4)
@Composable
fun WoodFramePreview() {
    WoodFrame(
        maxScreenWidth = PIXEL_4_VIEW_PORT.first,
        maxScreenHeight = PIXEL_4_VIEW_PORT.second,
        isSoundOn = true
    ) {}
}

@Preview(device = PIXEL)
@Composable
fun WoodFramePreview2() {
    WoodFrame(
        maxScreenWidth = PIXEL_1_VIEW_PORT.first,
        maxScreenHeight = PIXEL_1_VIEW_PORT.second,
        isSoundOn = true
    ) {}
}
