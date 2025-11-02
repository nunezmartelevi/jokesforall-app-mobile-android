package com.levi.jokesforall.ui.views

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import com.levi.jokesforall.ui.theme.JokesForAllTheme
import com.levi.jokesforall.ui.views.components.DisplayLayout
import com.levi.jokesforall.ui.views.components.ControlLayout
import com.levi.jokesforall.util.calculateDisplayPadding

@Composable
fun BoxWithConstraintsScope.EmptyView(
    modifier: Modifier = Modifier,
    isSoundOn: Boolean,
    onToggleSound: (Boolean) -> Unit
) {
    ControlLayout(
        modifier = modifier,
        maxScreenWidth = maxWidth,
        maxScreenHeight = maxHeight,
        isSoundOn = isSoundOn,
        onSoundButtonPress = { onToggleSound(isSoundOn) }
    )

    DisplayLayout(
        modifier = Modifier.calculateDisplayPadding(maxWidth, maxHeight),
        maxScreenWidth = maxWidth,
        maxScreenHeight = maxHeight,
        isSoundOn = isSoundOn,
        mainText = ""
    )
}

@Preview(device = PIXEL_4)
@Composable
fun EmptyViewPreview() {
    JokesForAllTheme {
        BoxWithConstraints {
            EmptyView(
                isSoundOn = true
            ) {}
        }
    }
}