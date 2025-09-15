package com.levi.jokesforall.ui.views.jokeviews

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import com.levi.jokesforall.R
import com.levi.jokesforall.ui.theme.JokesForAllTheme
import com.levi.jokesforall.ui.views.frames.WoodFrame
import com.levi.jokesforall.ui.views.frames.DisplayFrame
import com.levi.jokesforall.ui.views.frames.TextAnimationSpeed
import com.levi.jokesforall.util.calculateTextFramePadding

@Composable
fun BoxWithConstraintsScope.LoadingView(
    modifier: Modifier = Modifier,
    isSoundOn: Boolean,
    onToggleSound: (Boolean) -> Unit
) {
    WoodFrame(
        modifier = modifier,
        maxWidth = maxWidth,
        maxHeight = maxHeight,
        onSoundButtonPress = { onToggleSound(isSoundOn) }
    )

    DisplayFrame(
        modifier = Modifier.calculateTextFramePadding(maxWidth, maxHeight),
        maxHeight = maxHeight,
        isSoundOn = isSoundOn,
        mainText = stringResource(R.string.loading_message),
        textAnimationSpeed = TextAnimationSpeed.Normal
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
