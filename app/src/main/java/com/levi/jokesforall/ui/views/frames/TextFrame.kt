package com.levi.jokesforall.ui.views.frames

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import kotlinx.coroutines.delay
import java.text.BreakIterator
import java.text.StringCharacterIterator

@Composable
fun TextFrame(
    modifier: Modifier = Modifier,
    maxHeight: Dp,
    isSoundOn: Boolean,
    mainContentText: String,
    footerContent: @Composable (RowScope.(TextStyle) -> Unit) = {}
) {
    val calculatedFrameHeight = (maxHeight.value * 0.57).dp
    val text = mainContentText
    val breakIterator = remember(text) { BreakIterator.getCharacterInstance() }
    val typingDelayInMs = 50L
    var substringText by remember { mutableStateOf("") }
    var shouldDisplayFooterText by remember { mutableStateOf(false) }

    LaunchedEffect(text) {
        shouldDisplayFooterText = false
        breakIterator.text = StringCharacterIterator(text)
        var nextIndex = breakIterator.next()
        while (nextIndex != BreakIterator.DONE) {
            substringText = text.subSequence(0, nextIndex).toString()
            nextIndex = breakIterator.next()
            delay(typingDelayInMs)
        }
        delay(100)
        shouldDisplayFooterText = true
    }

    ConstraintLayout(
        modifier = modifier
            .background(DarkGrey)
            .height(calculatedFrameHeight)
            .padding(10.dp)
    ) {
        val (header, mainText, footer) = createRefs()
        Header(
            modifier = Modifier.constrainAs(header) {
                top.linkTo(parent.top)
            },
            isSoundOn = isSoundOn
        )
        MainContent(
            modifier = Modifier.constrainAs(mainText) {
                top.linkTo(header.bottom)
                bottom.linkTo(footer.top)
            },
            text = substringText
        )
        Footer(
            modifier = Modifier
                .constrainAs(footer) {
                    bottom.linkTo(parent.bottom)
                },
            isVisible = shouldDisplayFooterText
        ) {
            val textStyle = MaterialTheme.typography.bodyMedium
            footerContent(textStyle)
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    isSoundOn: Boolean
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val soundIcon = if (isSoundOn) R.drawable.ic_sound_on else R.drawable.ic_sound_off
        Image(
            painter = painterResource(soundIcon),
            modifier = Modifier.size(20.dp),
            contentDescription = null
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(painter = painterResource(R.drawable.ic_battery_full), null)
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
    isVisible: Boolean,
    content: @Composable (RowScope.() -> Unit)
) {
    Box(modifier = modifier.height(30.dp)) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(500)),
            exit = ExitTransition.None
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                content()
            }
        }
    }
}

@Preview(device = PIXEL_4)
@Composable
private fun TextFramePreview() {
    JokesForAllTheme {
        TextFrame(
            maxHeight = PIXEL_4_VIEW_PORT.second,
            isSoundOn = false,
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
