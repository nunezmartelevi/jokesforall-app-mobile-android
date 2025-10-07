package com.levi.jokesforall.ui.views.console

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.levi.jokesforall.R
import com.levi.jokesforall.ui.theme.JokesForAllTheme
import com.levi.jokesforall.util.PIXEL_4_VIEW_PORT
import com.levi.jokesforall.util.calculateDisplayHeight
import kotlinx.coroutines.delay
import java.text.BreakIterator
import java.text.StringCharacterIterator

@Composable
fun Display(
    modifier: Modifier = Modifier,
    maxScreenWidth: Dp,
    maxScreenHeight: Dp,
    isSoundOn: Boolean,
    mainText: String,
    textAnimationSpeed: TextAnimationSpeed = TextAnimationSpeed.Fast,
    onTextAnimationEnd: () -> Unit = {},
    onTextAnimationStart: () -> Unit = {},
    shouldDisplayFooter: Boolean = false,
    footerContent: @Composable (RowScope.(TextStyle) -> Unit) = {}
) {
    val maxScreenSize = maxScreenWidth + maxScreenHeight
    ConstraintLayout(
        modifier = modifier
            .calculateDisplayHeight(maxScreenHeight, maxScreenWidth)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        val (header, centerText, footer) = createRefs()

        Header(
            modifier = Modifier.constrainAs(header) {
                top.linkTo(parent.top)
            },
            isSoundOn = isSoundOn,
            maxScreenSize = maxScreenSize
        )

        CenterText(
            modifier = Modifier.constrainAs(centerText) {
                top.linkTo(header.bottom)
                bottom.linkTo(footer.top)
            },
            text = mainText,
            textAnimationSpeed = textAnimationSpeed,
            maxScreenSize = maxScreenSize,
            onAnimationStart = onTextAnimationStart,
            onAnimationEnd = onTextAnimationEnd
        )

        Footer(
            modifier = Modifier
                .constrainAs(footer) {
                    bottom.linkTo(parent.bottom)
                },
            isVisible = shouldDisplayFooter,
            maxScreenSize = maxScreenSize
        ) { footerStyle ->
            footerContent(footerStyle)
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    isSoundOn: Boolean,
    maxScreenSize: Dp
) {
    val soundIcon = if (isSoundOn) R.drawable.ic_sound_on else R.drawable.ic_sound_off
    val computedImageSize = (maxScreenSize / 55).value.dp

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(soundIcon),
            modifier = Modifier.size(computedImageSize),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            contentDescription = null
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier.size(computedImageSize + 3.dp),
            painter = painterResource(R.drawable.ic_battery_full),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            contentDescription = null
        )
    }
}

@Composable
private fun CenterText(
    modifier: Modifier = Modifier,
    text: String,
    textAnimationSpeed: TextAnimationSpeed,
    maxScreenSize: Dp,
    onAnimationStart: () -> Unit,
    onAnimationEnd: () -> Unit
) {
    val breakIterator = remember(text) { BreakIterator.getCharacterInstance() }
    var substringText by remember { mutableStateOf("") }
    val typingDelayInMs = textAnimationSpeed.value
    val computedFontSize = (maxScreenSize / 50).value.sp

    LaunchedEffect(text) {
        onAnimationStart()
        breakIterator.text = StringCharacterIterator(text)
        var nextIndex = breakIterator.next()
        while (nextIndex != BreakIterator.DONE) {
            substringText = text.subSequence(0, nextIndex).toString()
            nextIndex = breakIterator.next()
            delay(typingDelayInMs)
        }
        onAnimationEnd()
    }

    BasicText(
        modifier = modifier.fillMaxWidth(),
        text = substringText,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        ),
        maxLines = 12,
        autoSize = TextAutoSize.StepBased(
            minFontSize = (computedFontSize.value - 6).sp,
            maxFontSize = computedFontSize
        )
    )
}

@Composable
private fun Footer(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    maxScreenSize: Dp,
    content: @Composable (RowScope.(TextStyle) -> Unit)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val animatedColor by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        targetValue = MaterialTheme.colorScheme.onSurface.copy(alpha = 1f),
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse),
        label = "color"
    )
    val computedFontSize = (maxScreenSize / 55).value.sp
    val computedHeight = maxScreenSize / 40
    val style = MaterialTheme.typography.bodyMedium.copy(
        color = animatedColor,
        fontSize = computedFontSize,
    )

    Box(modifier = modifier.height(computedHeight)) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = 500,
                    delayMillis = 100
                )
            ),
            exit = ExitTransition.None
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                content(style)
            }
        }
    }
}

@Preview(device = PIXEL_4)
@Composable
private fun TextFramePreview() {
    JokesForAllTheme {
        Display(
            maxScreenWidth = PIXEL_4_VIEW_PORT.first,
            maxScreenHeight = PIXEL_4_VIEW_PORT.second,
            isSoundOn = false,
            mainText = "This is a joke",
            footerContent = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.action_next_joke),
                    textAlign = TextAlign.Center,
                    style = it
                )
            }
        )
    }
}
