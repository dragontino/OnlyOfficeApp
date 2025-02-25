package com.onlyoffice.app.ui.screens.main.profile

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import com.onlyoffice.app.R
import com.onlyoffice.app.ui.navigation.LocalBottomNavigationBarHeight
import com.onlyoffice.app.ui.theme.OnlyOfficeAppTheme
import com.onlyoffice.app.util.FullScreenContentWithLoading
import com.onlyoffice.app.util.FullScreenProgressIndicator
import com.onlyoffice.domain.model.account.UserInfo
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    navigateToEntryScreen: () -> Unit,
    showSnackbar: suspend (String) -> Unit,
    viewModel: ProfileViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        viewModel.messageFlow.collect { message ->
            showSnackbar(message)
        }
    }

    val loadingProgress = rememberSaveable {
        mutableStateOf<Float?>(null)
    }
    val animatedLoadingProgress = loadingProgress.value?.let {
        animateFloatAsState(
            targetValue = it,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )
    }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(bottom = LocalBottomNavigationBarHeight.current)
            .windowInsetsPadding(
                insets = WindowInsets.navigationBars
                    .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom)
            )
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.profile_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .windowInsetsPadding(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        FullScreenContentWithLoading(
            loadingState = remember {
                derivedStateOf { viewModel.isLoading }
            },
            loadingContent = {
                FullScreenProgressIndicator(animatedLoadingProgress)
            }
        ) {
            Crossfade(
                targetState = viewModel.userInfo,
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
            ) { userInfo ->
                when (userInfo) {
                    null -> Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "The data has not been downloaded",
                            style = MaterialTheme.typography.headlineLarge,
                            color = LocalContentColor.current.copy(alpha = .95f),
                            textAlign = TextAlign.Center
                        )
                    }

                    else -> UserInfoContent(
                        userInfo = userInfo,
                        onClickToLogoutButton = {
                            viewModel.logout(onSuccess = navigateToEntryScreen)
                        },
                        sendMessage = viewModel::sendMessage,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}


@Composable
private fun UserInfoContent(
    userInfo: UserInfo,
    onClickToLogoutButton: () -> Unit,
    sendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(IntrinsicSize.Max)
    ) {
        val accountCirclePainter = rememberVectorPainter(Icons.Rounded.AccountCircle)
        val asyncImagePainter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(userInfo.avatarUrl)
                .scale(Scale.FIT)
                .crossfade(true)
                .build(),
            onError = {
                it.result.throwable.localizedMessage?.takeIf { it.isNotBlank() }?.let(sendMessage)
            },
            placeholder = accountCirclePainter,
            error = accountCirclePainter
        )
        val state = asyncImagePainter.state.collectAsStateWithLifecycle()

        Image(
            painter = asyncImagePainter,
            contentDescription = "Profile logo",
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.primary.copy(alpha = .8f)
            ).takeUnless { state.value is AsyncImagePainter.State.Success },
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .width(screenWidth * .55f)
        )

        userInfo.displayName?.let { displayName ->
            Spacer(Modifier.height(32.dp))
            Text(
                text = displayName,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }

        userInfo.email?.let { email ->
            Spacer(Modifier.height(32.dp))
            Text(
                text = buildString {
                    appendLine(stringResource(R.string.email_placeholder))
                    append(email)
                },
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 24.sp,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(Modifier.height(48.dp))
        TextButton(
            onClick = onClickToLogoutButton,
            shape = MaterialTheme.shapes.extraLarge,
            elevation = ButtonDefaults.elevatedButtonElevation(),
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.width(screenWidth * .55f)
        ) {
            Text(
                text = stringResource(R.string.logout),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}


@Preview
@Composable
private fun UserInfoContentPreview() {
    OnlyOfficeAppTheme(darkTheme = false) {
        UserInfoContent(
            userInfo = UserInfo(
                id = "123",
                email = "test@email.com",
                displayName = "Test fon Test",
                avatarUrl = null
            ),
            onClickToLogoutButton = {},
            sendMessage = {},
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }

}