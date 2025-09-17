package com.levi.jokesforall.ui.views.jokeviews

import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.levi.jokesforall.R
import com.levi.jokesforall.ui.views.frames.WoodFrame
import com.levi.jokesforall.ui.views.frames.DisplayFrame
import com.levi.jokesforall.ui.views.frames.TextAnimationSpeed
import com.levi.jokesforall.util.calculateTextFramePadding
import kotlinx.coroutines.delay

@Composable
fun BoxWithConstraintsScope.IntroView(
    modifier: Modifier = Modifier,
    isSoundOn: Boolean,
    onToggleSound: (Boolean) -> Unit,
    onContinuePress: () -> Unit
) {
    val introTexts: Array<String> = stringArrayResource(R.array.intro_array)
    var isTextAnimationPlaying by remember { mutableStateOf(true) }
    val introListState = rememberIntroListState(introTexts.toList())
    val context = LocalContext.current
    val mediaPlayerState = rememberMediaPlayerState(context, R.raw.intro_music)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        val window = (context as? ComponentActivity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        lifecycleOwner.lifecycle.addObserver(mediaPlayerState)
        mediaPlayerState.startOrResumePlayback()

        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            mediaPlayerState.clearMediaPlayer()
            lifecycleOwner.lifecycle.removeObserver(mediaPlayerState)
        }
    }

    LaunchedEffect(isTextAnimationPlaying) {
        if (!isTextAnimationPlaying) {
            introListState.nextText()
            isTextAnimationPlaying = true
        }
    }

    LaunchedEffect(isSoundOn) {
        if (isSoundOn) {
            mediaPlayerState.unMute()
        } else {
            mediaPlayerState.mute()
        }
    }

    WoodFrame(
        modifier = modifier,
        maxWidth = maxWidth,
        maxHeight = maxHeight,
        isSoundOn = isSoundOn,
        onAButtonPress = { if (introListState.finished) onContinuePress() },
        onSoundButtonPress = { onToggleSound(isSoundOn) }
    )

    DisplayFrame(
        modifier = Modifier.calculateTextFramePadding(maxWidth, maxHeight),
        maxHeight = maxHeight,
        isSoundOn = isSoundOn,
        mainText = introListState.text,
        textAnimationSpeed = TextAnimationSpeed.Slow,
        onTextAnimationEnd = { isTextAnimationPlaying = false }
    ) { textStyle ->
        if (introListState.finished) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.action_continue),
                textAlign = TextAlign.Center,
                style = textStyle
            )
        }
    }
}

@Preview(device = PIXEL_4)
@Composable
fun IntroViewPreview() {
    BoxWithConstraints {
        IntroView(
            isSoundOn = true,
            onToggleSound = {}
        ) {}
    }
}

@Composable
fun rememberIntroListState(textList: List<String>): IntroListState =
    remember { IntroListState(textList) }

@Stable
class IntroListState(private val textList: List<String>) {
    private var currentTextIndex = 0
    var text = textList.firstOrNull() ?: ""
        private set
    var finished = false
        private set
    private val delayTimeInMillis = 1000L

    suspend fun nextText() {
        currentTextIndex++
        if (currentTextIndex >= textList.size) {
            finished = true
        } else {
            delay(delayTimeInMillis)
            text = textList[currentTextIndex]
        }
    }
}
