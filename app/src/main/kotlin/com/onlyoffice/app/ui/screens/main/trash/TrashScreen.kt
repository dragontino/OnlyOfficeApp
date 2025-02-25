package com.onlyoffice.app.ui.screens.main.trash

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.onlyoffice.app.R
import com.onlyoffice.app.ui.navigation.LocalBottomNavigationBarHeight
import com.onlyoffice.app.ui.screens.main.documents.DocumentsContent
import com.onlyoffice.app.util.AppBarDefaults
import com.onlyoffice.app.util.FullScreenContentWithLoading
import com.onlyoffice.app.util.FullScreenProgressIndicator
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrashScreen(
    showSnackbar: suspend (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TrashViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.messageFlow.collect { message ->
            showSnackbar(message)
        }
    }

    val animatedLoadingProgress = viewModel.loadingProgress?.let {
        animateFloatAsState(
            targetValue = it,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )
    }
    val layoutDirection = LocalLayoutDirection.current

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
            text = stringResource(R.string.trash_title),
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
                targetState = viewModel.documentsData,
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

                    else -> {

                    }
                }
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.trash_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBarDefaults.containerColor,
                    titleContentColor = AppBarDefaults.contentColor
                ),
                modifier = Modifier.fillMaxWidth()
            )
        },
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        modifier = modifier.fillMaxSize()
    ) { contentPadding ->
        FullScreenContentWithLoading(
            loadingContent = { FullScreenProgressIndicator(animatedLoadingProgress) },
            loadingState = remember {
                derivedStateOf { viewModel.isLoading }
            }
        ) {
            Crossfade(
                targetState = viewModel.documentsData?.takeUnless {
                    it.files.isEmpty() && it.folders.isEmpty()
                },
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
            ) { documentsData ->
                when (documentsData) {
                    null -> Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "The trash can is empty!",
                            style = MaterialTheme.typography.headlineLarge,
                            color = LocalContentColor.current.copy(alpha = .95f),
                            textAlign = TextAlign.Center
                        )
                    }

                    else -> DocumentsContent(
                        documentsData = documentsData,
                        contentPadding = PaddingValues(
                            top = contentPadding.calculateTopPadding(),
                            bottom = contentPadding.calculateBottomPadding() +
                                    LocalBottomNavigationBarHeight.current,
                            start = contentPadding.calculateStartPadding(layoutDirection),
                            end = contentPadding.calculateEndPadding(layoutDirection)
                        )
                    )
                }
            }
        }
    }
}