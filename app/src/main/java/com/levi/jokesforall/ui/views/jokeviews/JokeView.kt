package com.levi.jokesforall.ui.views.jokeviews

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.levi.jokesforall.R
import com.levi.jokesforall.domain.model.Joke
import com.levi.jokesforall.domain.model.JokeType
import com.levi.jokesforall.ui.theme.JokesForAllTheme
import com.levi.jokesforall.ui.views.JokesPreviewParameterProvider
import com.levi.jokesforall.ui.views.MediaPlayerVolumeEffect
import com.levi.jokesforall.ui.views.frames.WoodFrame
import com.levi.jokesforall.ui.views.frames.DisplayFrame
import com.levi.jokesforall.ui.views.rememberMediaPlayerState
import com.levi.jokesforall.util.calculateTextFramePadding

@Composable
fun BoxWithConstraintsScope.JokeView(
    modifier: Modifier = Modifier,
    isSoundOn: Boolean,
    joke: Joke,
    shouldDisplayPunchline: Boolean,
    onNextJoke: () -> Unit,
    onDisplayPunchline: () -> Unit,
    onHidePunchline: () -> Unit,
    onToggleSound: (Boolean) -> Unit
) {
    val jokeText = if (shouldDisplayPunchline) joke.delivery else joke.startingText
    var isTextAnimationPlaying by remember { mutableStateOf(false) }
    var shouldDisplayOptions by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val mediaPlayerState = rememberMediaPlayerState(context, R.raw.text_animation_sound)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        lifecycleOwner.lifecycle.addObserver(mediaPlayerState)
        mediaPlayerState.prepare()
        onDispose {
            mediaPlayerState.clearMediaPlayer()
            lifecycleOwner.lifecycle.removeObserver(mediaPlayerState)
        }
    }

    LaunchedEffect(isTextAnimationPlaying) {
        if (isTextAnimationPlaying) {
            mediaPlayerState.apply {
                resetPlaybackPosition()
                startOrResumePlayback()
                setResumingOnStartEnabled(true)
            }
        } else {
            mediaPlayerState.apply {
                pausePlayback()
                setResumingOnStartEnabled(false)
            }
        }
    }

    MediaPlayerVolumeEffect(isSoundOn, mediaPlayerState)

    WoodFrame(
        modifier = modifier,
        maxScreenWidth = maxWidth,
        maxScreenHeight = maxHeight,
        isSoundOn = isSoundOn,
        onAButtonPress = {
            if (!isTextAnimationPlaying) {
                shouldDisplayOptions = false
                when (joke.type) {
                    JokeType.SINGLE -> onNextJoke()
                    JokeType.TWOPART -> {
                        if (shouldDisplayPunchline) {
                            onNextJoke()
                        } else {
                            onDisplayPunchline()
                        }
                    }
                }
            }
        },
        onBButtonPress = {
            if (!isTextAnimationPlaying && shouldDisplayPunchline) {
                shouldDisplayOptions = false
                onHidePunchline()
            }
        },
        onSoundButtonPress = { onToggleSound(isSoundOn) }
    )

    DisplayFrame(
        modifier = Modifier.calculateTextFramePadding(maxWidth, maxHeight),
        maxScreenHeight = maxHeight,
        isSoundOn = isSoundOn,
        mainText = jokeText ?: "",
        onTextAnimationStart = { isTextAnimationPlaying = true },
        onTextAnimationEnd = {
            isTextAnimationPlaying = false
            shouldDisplayOptions = true
        }
    ) { textStyle ->
        if (shouldDisplayOptions) {
            when (joke.type) {
                JokeType.SINGLE -> {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.action_next_joke),
                        textAlign = TextAlign.End,
                        style = textStyle
                    )
                }

                JokeType.TWOPART -> {
                    if (shouldDisplayPunchline) {
                        Text(
                            text = stringResource(R.string.action_back_to_setup),
                            textAlign = TextAlign.Center,
                            style = textStyle
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = stringResource(R.string.action_next_joke),
                            textAlign = TextAlign.Center,
                            style = textStyle
                        )
                    } else {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.action_reveal_punchline),
                            textAlign = TextAlign.Center,
                            style = textStyle
                        )
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun TwoPartJokeViewPreview(
    @PreviewParameter(JokesPreviewParameterProvider::class) jokes: List<Joke>
) {
    JokesForAllTheme {
        BoxWithConstraints {
            JokeView(
                isSoundOn = true,
                joke = jokes[0],
                shouldDisplayPunchline = true,
                onNextJoke = {},
                onDisplayPunchline = {},
                onHidePunchline = {},
                onToggleSound = {}
            )
        }
    }
}
