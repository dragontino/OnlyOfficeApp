package com.onlyoffice.app.util

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import kotlin.math.roundToInt

object AnimationConstants {
    const val ANIMATION_TIME_MILLIS = 650
    const val SMALL_DELAY_TIME_PERCENT = .05f
    const val LONG_DELAY_TIME_PERCENT = .2f
}

fun <T> createAnimationSpec(
    animationTime: Int = AnimationConstants.ANIMATION_TIME_MILLIS,
    delayTimePercent: Float = AnimationConstants.SMALL_DELAY_TIME_PERCENT
): FiniteAnimationSpec<T> {
    val delayMillis = (animationTime * delayTimePercent).roundToInt()
    val durationMillis = animationTime - delayMillis
    return tween(durationMillis, delayMillis, easing = FastOutSlowInEasing)
}