package com.levi.jokesforall.util

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val PIXEL_4_VIEW_PORT = Pair(393.dp, 830.dp)
val PIXEL_1_VIEW_PORT = Pair(412.dp, 732.dp)

@Stable
fun Modifier.calculateTextFramePadding(maxWidth: Dp, maxHeight: Dp): Modifier {
    val hPadding = if (isHighWidthViewPort(maxWidth, maxHeight)) 40.dp else 17.dp
    val vPadding = if (isHighWidthViewPort(maxWidth, maxHeight)) 60.dp else 65.dp
    return this.padding(hPadding, vPadding)
}

/**
 * Checks if the viewport has a high width proportion relative to its height.
 * A viewport is considered to have a high width if its width is at least 55% of its height.
 *
 * @param width The width of the viewport.
 * @param height The height of the viewport.
 * @return True if the viewport has a high width, false otherwise.
 */
fun isHighWidthViewPort(width: Dp, height: Dp): Boolean {
    val highWidthPercentage = 55
    val widthProportionRelativeToHeight = (width.value / height.value) * 100
    return widthProportionRelativeToHeight >= highWidthPercentage
}
