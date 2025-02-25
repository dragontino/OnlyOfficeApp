package com.onlyoffice.app.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.BiasAbsoluteAlignment
import com.onlyoffice.app.util.AnimationConstants
import com.onlyoffice.app.util.createAnimationSpec
import kotlin.math.roundToInt

fun enterScreenTransition(
    expandFrom: BiasAbsoluteAlignment.Horizontal,
    animationTime: Int = AnimationConstants.ANIMATION_TIME_MILLIS,
    delayTimePercent: Float = AnimationConstants.SMALL_DELAY_TIME_PERCENT,
): EnterTransition {
    return slideInHorizontally(createAnimationSpec(animationTime, delayTimePercent)) { fullWidth ->
        (expandFrom.bias * fullWidth).roundToInt()
    }
}


fun exitScreenTransition(
    shrinkTowards: BiasAbsoluteAlignment.Horizontal,
    animationTime: Int = AnimationConstants.ANIMATION_TIME_MILLIS,
    delayTimePercent: Float = AnimationConstants.LONG_DELAY_TIME_PERCENT
): ExitTransition {
    return slideOutHorizontally(createAnimationSpec(animationTime, delayTimePercent)) { fullWidth ->
        (shrinkTowards.bias * fullWidth).roundToInt()
    }
}