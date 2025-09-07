package com.levi.jokesforall.ui.views.jokeviews

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.levi.jokesforall.R
import com.levi.jokesforall.domain.model.Joke
import com.levi.jokesforall.domain.model.JokeType
import com.levi.jokesforall.ui.theme.JokesForAllTheme
import com.levi.jokesforall.ui.views.JokesPreviewParameterProvider
import com.levi.jokesforall.ui.views.frames.WoodFrame
import com.levi.jokesforall.ui.views.frames.DisplayFrame
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
    val mainContentText = if (shouldDisplayPunchline) joke.delivery ?: "" else joke.setup
    var allowButtonInteraction by remember { mutableStateOf(false) }

    WoodFrame(
        modifier = modifier,
        maxWidth = maxWidth,
        maxHeight = maxHeight,
        onAButtonPress = {
            if (allowButtonInteraction) {
                when (joke.type) {
                    JokeType.SINGLE -> onNextJoke()
                    JokeType.TWOPART -> if (shouldDisplayPunchline) onNextJoke() else onDisplayPunchline()
                }
                allowButtonInteraction = false
            }
        },
        onBButtonPress = {
            if (allowButtonInteraction && shouldDisplayPunchline) {
                onHidePunchline()
                allowButtonInteraction = false
            }
        },
        onSoundButtonPress = { onToggleSound(isSoundOn) }
    )

    DisplayFrame(
        modifier = Modifier.calculateTextFramePadding(maxWidth, maxHeight),
        maxHeight = maxHeight,
        isSoundOn = isSoundOn,
        mainText = mainContentText,
        onTextAnimationEnd = { allowButtonInteraction = true }
    ) { textStyle ->
        if (allowButtonInteraction)
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
