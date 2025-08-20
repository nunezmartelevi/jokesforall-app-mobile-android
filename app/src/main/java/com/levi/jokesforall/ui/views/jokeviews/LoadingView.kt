package com.levi.jokesforall.ui.views.jokeviews

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import com.levi.jokesforall.R
import com.levi.jokesforall.ui.views.frames.ConsoleFrame
import com.levi.jokesforall.ui.views.frames.TextFrame
import com.levi.jokesforall.util.calculateTextFramePadding

@Composable
fun BoxWithConstraintsScope.LoadingView(
    modifier: Modifier = Modifier,
    batteryLevel: Int,
    isSoundOn: Boolean
) {
    ConsoleFrame(
        modifier = modifier,
        maxWidth = maxWidth,
        maxHeight = maxHeight
    )
    TextFrame(
        modifier = Modifier.calculateTextFramePadding(maxWidth, maxHeight),
        maxHeight = maxHeight,
        batteryLevel = batteryLevel,
        isSoundOn = isSoundOn,
        mainContentText = stringResource(R.string.loading)
    )
}

@Preview(device = PIXEL_4)
@Composable
fun LoadingViewPreview() {
    BoxWithConstraints {
        LoadingView(
            batteryLevel = 61,
            isSoundOn = true
        )
    }
}
