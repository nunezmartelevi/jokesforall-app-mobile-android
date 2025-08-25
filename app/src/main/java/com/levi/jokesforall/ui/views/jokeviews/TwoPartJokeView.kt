package com.levi.jokesforall.ui.views.jokeviews

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.levi.jokesforall.R
import com.levi.jokesforall.domain.model.Joke
import com.levi.jokesforall.ui.theme.GreenTerminal
import com.levi.jokesforall.ui.theme.JokesForAllTheme
import com.levi.jokesforall.ui.views.JokesPreviewParameterProvider
import com.levi.jokesforall.ui.views.frames.ConsoleFrame
import com.levi.jokesforall.ui.views.frames.TextFrame
import com.levi.jokesforall.util.calculateTextFramePadding
import java.text.BreakIterator

@Composable
fun BoxWithConstraintsScope.TwoPartJokeView(
    modifier: Modifier = Modifier,
    isSoundOn: Boolean,
    joke: Joke,
    canShowDelivery: Boolean,
    canGoBack: Boolean,
    onNextJoke: () -> Unit,
    onPreviousJoke: () -> Unit,
    onShowDelivery: () -> Unit,
    onToggleSound: () -> Unit
) {
    val mainContentText = if (canShowDelivery) joke.delivery ?: "" else joke.setup
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val animatedColor by infiniteTransition.animateColor(
        initialValue = GreenTerminal.copy(alpha = 0.5f),
        targetValue = GreenTerminal.copy(alpha = 1f),
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse),
        label = "color"
    )

    ConsoleFrame(
        modifier = modifier,
        maxWidth = maxWidth,
        maxHeight = maxHeight,
        onAButtonPress = { if (canShowDelivery) onNextJoke() else onShowDelivery() },
        onBButtonPress = onPreviousJoke,
        onSoundButtonPress = onToggleSound
    )

    TextFrame(
        modifier = Modifier.calculateTextFramePadding(maxWidth, maxHeight),
        maxHeight = maxHeight,
        isSoundOn = isSoundOn,
        mainContentText = mainContentText
    ) { textStyle ->
        when {
            canShowDelivery && canGoBack -> {
                Text(
                    text = stringResource(R.string.previous_joke),
                    textAlign = TextAlign.Center,
                    style = textStyle
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.next_joke),
                    textAlign = TextAlign.Center,
                    style = textStyle
                )
            }

            canShowDelivery -> {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.continue_next_joke),
                    textAlign = TextAlign.Center,
                    style = textStyle
                )
            }

            else -> {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.reveal_joke),
                    textAlign = TextAlign.Center,
                    style = textStyle.copy(color = animatedColor)
                )
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
            TwoPartJokeView(
                isSoundOn = true,
                joke = jokes[0],
                canShowDelivery = true,
                canGoBack = true,
                onNextJoke = {},
                onPreviousJoke = {},
                onShowDelivery = {},
                onToggleSound = {}
            )
        }
    }
}
