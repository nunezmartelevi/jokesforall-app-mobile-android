package com.levi.jokesforall.util

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val PIXEL_4_VIEW_PORT = Pair(393.dp, 830.dp)

/**
 * Calculates and applies padding to a Composable based on the screen's dimensions.
 *
 * This function is a [Modifier] extension that adjusts the horizontal and vertical padding
 * depending on whether the viewport is considered "wider". A wider viewport, as determined by
 * [isWiderViewPort], receives more horizontal padding and slightly less vertical padding.
 * This helps in creating a responsive layout that adapts to different screen aspect ratios.
 *
 * @param maxWidth The maximum width of the available display area.
 * @param maxHeight The maximum height of the available display area.
 * @return A new [Modifier] with the calculated padding applied.
 */
@Stable
fun Modifier.calculateDisplayPadding(maxWidth: Dp, maxHeight: Dp): Modifier {
    val hPadding = if (isWiderViewPort(maxWidth, maxHeight)) 20.dp else 10.dp
    val vPadding = if (isWiderViewPort(maxWidth, maxHeight)) 63.dp else 66.dp
    return this.padding(hPadding, vPadding)
}

/**
 * Calculates the display height for a composable based on the screen's dimensions.
 *
 * This modifier function sets the height of a composable to a percentage of the maximum available
 * height. The percentage used depends on whether the screen is considered to have a "wider"
 * viewport, as determined by [isWiderViewPort].
 *
 * - For wider viewports, it sets the height to 57% of [maxHeight].
 * - For other viewports, it sets the height to 59% of [maxHeight].
 *
 * This is useful for creating responsive layouts that adapt to different screen aspect ratios.
 *
 * @param maxHeight The maximum available height of the screen or parent composable.
 * @param maxWidth The maximum available width of the screen or parent composable, used to determine the aspect ratio.
 * @return A [Modifier] with the calculated height applied.
 */
@Stable
fun Modifier.calculateDisplayHeight(maxHeight: Dp, maxWidth: Dp): Modifier {
    val displayHeightPercentage = if (isWiderViewPort(maxWidth, maxHeight)) 0.57 else 0.59
    return this.height((maxHeight.value * displayHeightPercentage).dp)
}

/**
 * Checks if the viewport has a high width proportion relative to its height.
 * A viewport is considered to have a high width if its width is at least 55% of its height.
 *
 * @param width The width of the viewport.
 * @param height The height of the viewport.
 * @return True if the viewport has a high width, false otherwise.
 */
fun isWiderViewPort(width: Dp, height: Dp): Boolean {
    val widthPercentageRelativeToHeight = 47
    val calculatedWidth = (width.value / height.value) * 100
    return calculatedWidth >= widthPercentageRelativeToHeight
}
