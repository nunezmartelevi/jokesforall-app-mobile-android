package com.levi.jokesforall.ui.views.console

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

/**
 * Defines the speed of the text typing animation.
 * This sealed interface allows for a predefined set of speeds (e.g., Fast, Slow)
 * or custom speeds through the [Speed] data class.
 *
 * @see Speed for a custom speed implementation.
 * @see Companion for predefined speed constants.
 */
@Stable
sealed interface TextAnimationSpeed {

    val value: Long
    companion object {
        @Stable
        val Slow = Speed(100L)
        @Stable
        val Normal = Speed(75L)
        @Stable
        val Fast = Speed(50L)
    }

    @Immutable
    data class Speed(override val value: Long) : TextAnimationSpeed
}
