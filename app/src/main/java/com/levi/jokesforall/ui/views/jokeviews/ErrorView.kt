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
import com.levi.jokesforall.ui.views.frames.ConsoleFrame
import com.levi.jokesforall.ui.views.frames.TextFrame
import com.levi.jokesforall.util.calculateTextFramePadding

@Composable
fun BoxWithConstraintsScope.ErrorView(
    modifier: Modifier = Modifier,
    batteryLevel: Int,
    isSoundOn: Boolean,
    onRetry: () -> Unit
) {
    ConsoleFrame(
        modifier = modifier,
        maxWidth = maxWidth,
        maxHeight = maxHeight,
        onAButtonPress = onRetry
    )

    TextFrame(
        modifier = Modifier.calculateTextFramePadding(maxWidth, maxHeight),
        maxHeight = maxHeight,
        batteryLevel = batteryLevel,
        isSoundOn = isSoundOn,
        mainContentText = stringResource(R.string.error),
    ) { textStyle ->
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.continue_next_joke),
            textAlign = TextAlign.Center,
            style = textStyle
        )
    }
}

@Preview
@Composable
fun ErrorViewPreview() {
    BoxWithConstraints {
        ErrorView(
            batteryLevel = 61,
            isSoundOn = true
        ) {}
    }
}
