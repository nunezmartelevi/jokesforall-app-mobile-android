package com.levi.jokesforall.ui.views.jokeviews

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.levi.jokesforall.R
import com.levi.jokesforall.ui.theme.JokesForAllTheme
import com.levi.jokesforall.ui.views.frames.WoodFrame
import com.levi.jokesforall.ui.views.frames.DisplayFrame
import com.levi.jokesforall.util.calculateTextFramePadding

@Composable
fun BoxWithConstraintsScope.ErrorView(
    modifier: Modifier = Modifier,
    isSoundOn: Boolean,
    onToggleSound: (Boolean) -> Unit,
    onRetry: () -> Unit
) {
    WoodFrame(
        modifier = modifier,
        maxWidth = maxWidth,
        maxHeight = maxHeight,
        onAButtonPress = onRetry,
        onSoundButtonPress = { onToggleSound(isSoundOn) }
    )

    DisplayFrame(
        modifier = Modifier.calculateTextFramePadding(maxWidth, maxHeight),
        maxHeight = maxHeight,
        isSoundOn = isSoundOn,
        mainText = stringResource(R.string.error_message),
    ) { textStyle ->
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.action_retry),
            textAlign = TextAlign.Center,
            style = textStyle
        )
    }
}

@Preview
@Composable
fun ErrorViewPreview() {
    JokesForAllTheme {
        BoxWithConstraints {
            ErrorView(
                isSoundOn = true,
                onToggleSound = {}
            ) {}
        }
    }
}
