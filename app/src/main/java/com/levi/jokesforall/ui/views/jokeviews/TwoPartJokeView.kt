package com.levi.jokesforall.ui.views.jokeviews

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.levi.jokesforall.R
import com.levi.jokesforall.domain.model.Joke
import com.levi.jokesforall.ui.views.JokesPreviewParameterProvider
import com.levi.jokesforall.ui.views.frames.ConsoleFrame
import com.levi.jokesforall.ui.views.frames.TextFrame
import com.levi.jokesforall.util.calculateTextFramePadding

@Composable
fun BoxWithConstraintsScope.TwoPartJokeView(
    modifier: Modifier = Modifier,
    batteryLevel: Int,
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
        batteryLevel = batteryLevel,
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
                    style = textStyle
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
    BoxWithConstraints {
        TwoPartJokeView(
            batteryLevel = 61,
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
