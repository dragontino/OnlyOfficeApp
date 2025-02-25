package com.onlyoffice.app.ui.screens.main.documents

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.onlyoffice.app.R
import com.onlyoffice.app.ui.navigation.LocalBottomNavigationBarHeight
import com.onlyoffice.app.util.AppBarDefaults
import com.onlyoffice.app.util.FullScreenContentWithLoading
import com.onlyoffice.app.util.FullScreenProgressIndicator
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentsScreen(
    showSnackbar: suspend (String) -> Unit,
    viewModel: DocumentsViewModel = koinViewModel(),
    modifier: Modifier = Modifier
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

    BackHandler(enabled = viewModel.documentsData?.currentFolder?.isRootFolder() == false) {
        viewModel.openParentFolder()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = viewModel.documentsData?.currentFolder?.title
                            ?: stringResource(R.string.documents_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    AnimatedVisibility(
                        visible = viewModel.documentsData?.currentFolder?.isRootFolder() == false,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        IconButton(onClick = viewModel::openParentFolder) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Arrow Back icon"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBarDefaults.containerColor,
                    titleContentColor = AppBarDefaults.contentColor,
                    navigationIconContentColor = AppBarDefaults.contentColor
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
                    it.currentFolder.run { foldersCount + filesCount == 0 }
                },
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
            ) { documentsData ->
                when (documentsData) {
                    null -> Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(bottom = LocalBottomNavigationBarHeight.current)
                            .fillMaxSize()
                    ) {
                        Text(
                            text = stringResource(R.string.empty_folder_placeholder),
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
                        ),
                        onClickToFolder = viewModel::openChildFolder,
                        onClickToFile = viewModel::openFile
                    )
                }
            }
        }
    }
}