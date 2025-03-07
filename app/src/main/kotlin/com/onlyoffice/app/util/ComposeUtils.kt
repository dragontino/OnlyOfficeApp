package com.onlyoffice.app.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val Color.inverse: Color get() = copy(red = 1 - red, green = 1 - green, blue = 1 - blue)


@Composable
fun FullScreenContentWithLoading(
    loadingContent: @Composable () -> Unit,
    loadingState: State<Boolean>,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background.copy(alpha = .9f),
    mainContent: @Composable () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        mainContent()

        AnimatedVisibility(
            visible = loadingState.value,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .background(backgroundColor)
                .fillMaxSize()
        ) {
            loadingContent()
        }
    }
}


@Composable
fun FullScreenProgressIndicator(
    progress: State<Float>?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = when (progress) {
                null -> null
                else -> {
                    { progress.value }
                }
            },
            strokeWidth = 3.dp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(16.dp)
                .scale(2f)
        )
    }
}


@Composable
fun CircularProgressIndicator(
    progress: (() -> Float)?,
    modifier: Modifier = Modifier,
    color: Color = ProgressIndicatorDefaults.circularColor,
    strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth,
    trackColor: Color = ProgressIndicatorDefaults.circularDeterminateTrackColor,
    strokeCap: StrokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap
) {
    when (progress) {
        null -> CircularProgressIndicator(modifier, color, strokeWidth, trackColor, strokeCap)
        else -> CircularProgressIndicator(
            progress, modifier, color, strokeWidth, trackColor, strokeCap
        )
    }
}


object AppBarDefaults {
    val containerColor: Color
        @Composable get() = MaterialTheme.colorScheme.background.copy(alpha = .8f)

    val contentColor: Color
        @Composable get() = MaterialTheme.colorScheme.onBackground
}


@Composable
fun TwoRowsSnackbar(
    snackbarData: SnackbarData,
    shape: Shape = MaterialTheme.shapes.medium,
    containerColor: Color = MaterialTheme.colorScheme.background.inverse,
    contentColor: Color = MaterialTheme.colorScheme.onBackground.inverse,
    modifier: Modifier = Modifier
) {
    Snackbar(
        action = snackbarData.visuals.actionLabel?.let { actionLabel ->
            @Composable {
                TextButton(
                    onClick = snackbarData::performAction,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = LocalContentColor.current
                    )
                ) {
                    Text(
                        text = actionLabel,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        },
        dismissAction = when {
            snackbarData.visuals.withDismissAction -> {
                @Composable {
                    IconButton(
                        onClick = snackbarData::dismiss,
                        content = {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Dismiss snackbar",
                            )
                        }
                    )
                }
            }

            else -> null
        },
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        actionContentColor = MaterialTheme.colorScheme.inversePrimary,
        dismissActionContentColor = MaterialTheme.colorScheme.inversePrimary,
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = snackbarData.visuals.message,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}