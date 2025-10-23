package com.levi.jokesmachine.ui.views

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
import com.levi.jokesmachine.R
import com.levi.jokesmachine.ui.views.components.MediaPlayerVolumeEffect
import com.levi.jokesmachine.ui.views.components.DisplayLayout
import com.levi.jokesmachine.ui.views.components.TextAnimationSpeed
import com.levi.jokesmachine.ui.views.components.ControlLayout
import com.levi.jokesmachine.ui.views.components.rememberMediaPlayerState
import com.levi.jokesmachine.util.calculateDisplayPadding
import kotlinx.coroutines.delay

@Composable
fun BoxWithConstraintsScope.IntroView(
    modifier: Modifier = Modifier,
    isSoundOn: Boolean,
    onToggleSound: (Boolean) -> Unit,
    onContinuePress: () -> Unit
) {
    val introTexts: Array<String> = stringArrayResource(R.array.intro_array)
    val introListState = rememberIntroListState(introTexts.toList())
    var isTextAnimationPlaying by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val mediaPlayerState = rememberMediaPlayerState(context, R.raw.intro_music)
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(Unit) {
        val window = (context as? ComponentActivity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        lifecycleOwner.lifecycle.addObserver(mediaPlayerState)
        mediaPlayerState.apply {
            setLoopingEnabled(true)
            prepare(shouldStartPlaybackAfterPrepared = true)
            setResumingOnStartEnabled(true)
        }

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

    MediaPlayerVolumeEffect(isSoundOn, mediaPlayerState)

    ControlLayout(
        modifier = modifier,
        maxScreenWidth = maxWidth,
        maxScreenHeight = maxHeight,
        isSoundOn = isSoundOn,
        onAButtonPress = { if (introListState.finished) onContinuePress() },
        onBButtonPress = { if (introListState.canSkip) onContinuePress() },
        onSoundButtonPress = { onToggleSound(isSoundOn) }
    )

    DisplayLayout(
        modifier = Modifier.calculateDisplayPadding(maxWidth, maxHeight),
        maxScreenWidth = maxWidth,
        maxScreenHeight = maxHeight,
        isSoundOn = isSoundOn,
        mainText = introListState.text,
        textAnimationSpeed = TextAnimationSpeed.Slow,
        onTextAnimationEnd = { isTextAnimationPlaying = false },
        shouldDisplayFooter = introListState.canSkip || introListState.finished
    ) { textStyle ->
        if (introListState.canSkip) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.action_skip),
                textAlign = TextAlign.Start,
                style = textStyle
            )
        } else if (introListState.finished) {
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

    var canSkip by mutableStateOf(false)
        private set
    var finished = false
        private set

    suspend fun nextText() {
        currentTextIndex++
        if (currentTextIndex >= textList.size) {
            canSkip = false
            delay(100)
            finished = true
        } else {
            delay(1000)
            text = textList[currentTextIndex]
            canSkip = currentTextIndex >= (textList.size / 2) - 1
        }
    }
}
