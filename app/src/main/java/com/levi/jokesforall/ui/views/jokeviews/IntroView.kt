package com.levi.jokesforall.ui.views.jokeviews

import android.view.WindowManager
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
    val state = rememberIntroState(introTexts.toList())
    var isAnimating by remember { mutableStateOf(true) }
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val window = (context as? androidx.activity.ComponentActivity)?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        onDispose {
            window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    LaunchedEffect(isAnimating) {
        if (!isAnimating) {
            state.nextIntroText()
            isAnimating = true
        }
    }

    WoodFrame(
        modifier = modifier,
        maxWidth = maxWidth,
        maxHeight = maxHeight,
        onAButtonPress = { if (state.introEnded) onContinuePress() },
        onSoundButtonPress = { onToggleSound(isSoundOn) }
    )

    DisplayFrame(
        modifier = Modifier.calculateTextFramePadding(maxWidth, maxHeight),
        maxHeight = maxHeight,
        isSoundOn = isSoundOn,
        mainText = state.text,
        textAnimationSpeed = TextAnimationSpeed.Slow,
        onTextAnimationEnd = { isAnimating = false }
    ) { textStyle ->
        if (state.introEnded) {
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
fun rememberIntroState(textList: List<String>): IntroState {
    return remember { IntroState(textList) }
}

@Stable
class IntroState(private val textList: List<String>) {
    private val delayTimeInMillis = 1000L
    private var currentTextIndex = 0
    var text = textList.firstOrNull() ?: ""
        private set
    var introEnded = false
        private set

    init {
        require(textList.isNotEmpty()) { "textList cannot be empty" }
    }

    suspend fun nextIntroText() {
        currentTextIndex++
        if (currentTextIndex >= textList.size) {
            introEnded = true
        } else {
            delay(delayTimeInMillis)
            text = textList[currentTextIndex]
        }
    }
}
