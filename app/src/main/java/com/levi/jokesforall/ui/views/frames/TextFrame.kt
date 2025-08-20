package com.levi.jokesforall.ui.views.frames

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.levi.jokesforall.R
import com.levi.jokesforall.ui.theme.DarkGrey
import com.levi.jokesforall.ui.theme.JokesForAllTheme
import com.levi.jokesforall.util.PIXEL_4_VIEW_PORT

@Composable
fun TextFrame(
    modifier: Modifier = Modifier,
    maxHeight: Dp,
    batteryLevel: Int,
    isSoundOn: Boolean,
    mainContentText: String,
    footerContent: @Composable (RowScope.(TextStyle) -> Unit) = {}
) {
    val frameHeight = (maxHeight.value * 0.57).dp
    val soundLabel = if (isSoundOn) R.string.sound_on else R.string.sound_off
    val batteryLabel = R.string.battery_level

    ConstraintLayout(
        modifier = modifier
            .background(DarkGrey)
            .height(frameHeight)
            .padding(10.dp)
    ) {
        val (header, mainText, footer) = createRefs()
        Header(
            modifier = Modifier.constrainAs(header) {
                top.linkTo(parent.top)
            },
            soundLabel = stringResource(soundLabel),
            batteryLabel = stringResource(batteryLabel, batteryLevel)
        )
        MainContent(
            modifier = Modifier.constrainAs(mainText) {
                top.linkTo(header.bottom)
                bottom.linkTo(footer.top)
            },
            text = mainContentText
        )
        Footer(
            modifier = Modifier.constrainAs(footer) {
                bottom.linkTo(parent.bottom, margin = 20.dp)
            }
        ) {
            footerContent(MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    soundLabel: String,
    batteryLabel: String
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            text = batteryLabel,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = soundLabel,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun Footer(
    modifier: Modifier = Modifier,
    content: @Composable (RowScope.() -> Unit)
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        content()
    }
}

@Preview(device = PIXEL_4)
@Composable
private fun TextFramePreview() {
    JokesForAllTheme {
        TextFrame(
            maxHeight = PIXEL_4_VIEW_PORT.second,
            batteryLevel = 80,
            isSoundOn = true,
            mainContentText = "This is a joke",
            footerContent = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.continue_next_joke),
                    textAlign = TextAlign.Center,
                    style = it
                )
            }
        )
    }
}
