package com.levi.jokesforall.ui.views.jokeviews

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.levi.jokesforall.R
import com.levi.jokesforall.ui.theme.JokesForAllTheme
import com.levi.jokesforall.ui.views.MediaPlayerVolumeEffect
import com.levi.jokesforall.ui.views.console.Controls
import com.levi.jokesforall.ui.views.console.Display
import com.levi.jokesforall.ui.views.console.TextAnimationSpeed
import com.levi.jokesforall.ui.views.rememberMediaPlayerState
import com.levi.jokesforall.util.calculateTextFramePadding

@Composable
fun BoxWithConstraintsScope.LoadingView(
    modifier: Modifier = Modifier,
    isSoundOn: Boolean,
    onToggleSound: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val mediaPlayerState = rememberMediaPlayerState(context, R.raw.loading_sound)
    val lifecycleOwner = LocalLifecycleOwner.current
    var shouldDisplayFooter by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        lifecycleOwner.lifecycle.addObserver(mediaPlayerState)
        mediaPlayerState.apply {
            setLoopingEnabled(true)
            prepare(shouldStartPlaybackAfterPrepared = true)
            setResumingOnStartEnabled(true)
        }

        onDispose {
            mediaPlayerState.clearMediaPlayer()
            lifecycleOwner.lifecycle.removeObserver(mediaPlayerState)
        }
    }

    MediaPlayerVolumeEffect(isSoundOn, mediaPlayerState)

    Controls(
        modifier = modifier,
        maxScreenWidth = maxWidth,
        maxScreenHeight = maxHeight,
        isSoundOn = isSoundOn,
        onSoundButtonPress = { onToggleSound(isSoundOn) }
    )

    Display(
        modifier = Modifier.calculateTextFramePadding(maxWidth, maxHeight),
        maxScreenHeight = maxHeight,
        isSoundOn = isSoundOn,
        mainText = stringResource(R.string.loading_message),
        textAnimationSpeed = TextAnimationSpeed.Normal,
        onTextAnimationEnd = { shouldDisplayFooter = true },
        shouldDisplayFooter = shouldDisplayFooter
    ) { textStyle ->
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.please_wait_message),
            textAlign = TextAlign.Center,
            style = textStyle
        )
    }
}

@Preview(device = PIXEL_4)
@Composable
fun LoadingViewPreview() {
    JokesForAllTheme {
        BoxWithConstraints {
            LoadingView(
                isSoundOn = true
            ) {}
        }
    }
}
