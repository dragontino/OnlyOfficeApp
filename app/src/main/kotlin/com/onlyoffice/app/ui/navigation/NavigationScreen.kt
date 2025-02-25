package com.onlyoffice.app.ui.navigation

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Dataset
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Dataset
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.onlyoffice.app.R
import com.onlyoffice.app.ui.screens.entry.EntryScreen
import com.onlyoffice.app.ui.screens.main.documents.DocumentsScreen
import com.onlyoffice.app.ui.screens.main.profile.ProfileScreen
import com.onlyoffice.app.ui.screens.main.rooms.RoomsScreen
import com.onlyoffice.app.ui.screens.main.trash.TrashScreen
import com.onlyoffice.app.ui.theme.RobotoFont
import com.onlyoffice.app.util.AnimationConstants
import com.onlyoffice.app.util.AppBarDefaults
import com.onlyoffice.app.util.createAnimationSpec
import com.onlyoffice.app.util.reversed

val LocalBottomNavigationBarHeight = compositionLocalOf {
    0.dp
}

@Composable
fun NavigationScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val selectedBottomBarItem = remember {
        derivedStateOf {
            listOf(
                BottomBarItem.Documents,
                BottomBarItem.Rooms,
                BottomBarItem.Trash,
                BottomBarItem.Profile
            ).find {
                currentBackStackEntry.value?.destination?.route == it.destination.name
            } ?: BottomBarItem.Documents
        }
    }

    val snackbarHostState = remember(::SnackbarHostState)

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = NavDestinations.entries.any {
                    it != NavDestinations.Entry && with(currentBackStackEntry) {
                        value?.destination?.route == null || value?.destination?.route == it.name
                    }
                },
                enter = expandVertically(createAnimationSpec()),
                exit = shrinkVertically(
                    animationSpec = createAnimationSpec(
                        delayTimePercent = AnimationConstants.LONG_DELAY_TIME_PERCENT
                    )
                )
            ) {
                BottomBar(
                    selectedItem = selectedBottomBarItem.value,
                    onClickToItem = {
                        if (it.destination.name != navController.currentDestination?.route) {
                            navController.navigate(it.destination.name) {
                                popUpTo(NavDestinations.Documents.name)
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    dismissAction = {
                        IconButton(
                            onClick = data::dismiss,
                            content = {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Dismiss snackbar",
                                )
                            }
                        )
                    },
                    shape = MaterialTheme.shapes.medium,
                    containerColor = MaterialTheme.colorScheme.background.reversed,
                    contentColor = MaterialTheme.colorScheme.onBackground.reversed,
                    dismissActionContentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = data.visuals.message,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets(0),
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = contentPadding.calculateEndPadding(LocalLayoutDirection.current)
                )
                .fillMaxSize(),
        ) {
            CompositionLocalProvider(LocalBottomNavigationBarHeight provides contentPadding.calculateBottomPadding()) {
                NavHost(
                    navController = navController,
                    startDestination = NavDestinations.Entry.name,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxSize()
                ) {
                    composable(
                        route = NavDestinations.Entry.name,
                        enterTransition = { fadeIn(createAnimationSpec()) },
                        exitTransition = {
                            fadeOut(
                                animationSpec = createAnimationSpec(
                                    delayTimePercent = AnimationConstants.LONG_DELAY_TIME_PERCENT
                                )
                            )
                        }
                    ) {
                        EntryScreen(
                            navigateToMainScreen = {
                                navController.navigate(NavDestinations.Main.name) {
                                    popUpTo(route = NavDestinations.Entry.name) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            },
                            showSnackbar = {
                                snackbarHostState.showSnackbar(it, withDismissAction = true)
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }


                    navigation(
                        route = NavDestinations.Main.name,
                        startDestination = NavDestinations.Documents.name
                    ) {
                        composable(
                            route = NavDestinations.Documents.name,
                            enterTransition = {
                                enterScreenTransition(
                                    AbsoluteAlignment.Left as BiasAbsoluteAlignment.Horizontal
                                )
                            },
                            exitTransition = {
                                exitScreenTransition(
                                    AbsoluteAlignment.Left as BiasAbsoluteAlignment.Horizontal
                                )
                            }
                        ) {
                            DocumentsScreen(
                                showSnackbar = {
                                    snackbarHostState.showSnackbar(it, withDismissAction = true)
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        composable(
                            route = NavDestinations.Rooms.name,
                            enterTransition = {
                                val expandFrom = when (initialState.destination.route) {
                                    NavDestinations.Documents.name -> AbsoluteAlignment.Right
                                    else -> AbsoluteAlignment.Left
                                }
                                enterScreenTransition(expandFrom as BiasAbsoluteAlignment.Horizontal)
                            },
                            exitTransition = {
                                val shrinkTowards = when (targetState.destination.route) {
                                    NavDestinations.Documents.name -> AbsoluteAlignment.Right
                                    else -> AbsoluteAlignment.Left
                                }
                                exitScreenTransition(shrinkTowards as BiasAbsoluteAlignment.Horizontal)
                            }
                        ) {
                            RoomsScreen(
                                showSnackbar = {
                                    snackbarHostState.showSnackbar(it, withDismissAction = true)
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        composable(
                            route = NavDestinations.Trash.name,
                            enterTransition = {
                                val expandFrom = when (initialState.destination.route) {
                                    NavDestinations.Trash.name -> AbsoluteAlignment.Left
                                    else -> AbsoluteAlignment.Right
                                }
                                enterScreenTransition(expandFrom as BiasAbsoluteAlignment.Horizontal)
                            },
                            exitTransition = {
                                val shrinkTowards = when (targetState.destination.route) {
                                    NavDestinations.Trash.name -> AbsoluteAlignment.Left
                                    else -> AbsoluteAlignment.Right
                                }
                                exitScreenTransition(shrinkTowards as BiasAbsoluteAlignment.Horizontal)
                            }
                        ) {
                            TrashScreen(
                                showSnackbar = {
                                    snackbarHostState.showSnackbar(it, withDismissAction = true)
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        composable(
                            route = NavDestinations.Profile.name,
                            enterTransition = {
                                enterScreenTransition(
                                    AbsoluteAlignment.Right as BiasAbsoluteAlignment.Horizontal
                                )
                            },
                            exitTransition = {
                                exitScreenTransition(
                                    AbsoluteAlignment.Right as BiasAbsoluteAlignment.Horizontal
                                )
                            }
                        ) {
                            ProfileScreen(
                                navigateToEntryScreen = {
                                    navController.navigate(NavDestinations.Entry.name) {
                                        popUpTo(route = NavDestinations.Main.name) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                },
                                showSnackbar = {
                                    snackbarHostState.showSnackbar(it, withDismissAction = true)
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}




private sealed class BottomBarItem(
    val selectedIcon: ImageVector,
    val defaultIcon: ImageVector,
    val text: (Context) -> String,
    val destination: NavDestinations
) {
    data object Documents : BottomBarItem(
        selectedIcon = Icons.Rounded.Description,
        defaultIcon = Icons.Outlined.Description,
        text = { it.getString(R.string.documents_title) },
        destination = NavDestinations.Documents
    )

    data object Rooms : BottomBarItem(
        selectedIcon = Icons.Rounded.Dataset,
        defaultIcon = Icons.Outlined.Dataset,
        text = { it.getString(R.string.rooms_title) },
        destination = NavDestinations.Rooms
    )

    data object Trash : BottomBarItem(
        selectedIcon = Icons.Rounded.Delete,
        defaultIcon = Icons.Outlined.Delete,
        text = { it.getString(R.string.trash_title) },
        destination = NavDestinations.Trash
    )

    data object Profile : BottomBarItem(
        selectedIcon = Icons.Rounded.AccountCircle,
        defaultIcon = Icons.Outlined.AccountCircle,
        text = { it.getString(R.string.profile_title) },
        destination = NavDestinations.Profile
    )
}


@Composable
private fun BottomBar(
    selectedItem: BottomBarItem,
    onClickToItem: (BottomBarItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    NavigationBar(
        containerColor = AppBarDefaults.containerColor,
        contentColor = AppBarDefaults.contentColor,
        modifier = modifier.fillMaxWidth()
    ) {
        arrayOf(
            BottomBarItem.Documents,
            BottomBarItem.Rooms,
            BottomBarItem.Trash,
            BottomBarItem.Profile
        ).forEach { item ->
            NavigationBarItem(
                selected = item == selectedItem,
                onClick = { onClickToItem(item) },
                icon = {
                    Crossfade(
                        targetState = item,
                        label = "navigation bar icon cross fade",
                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                    ) {
                        Icon(
                            imageVector = when (it) {
                                selectedItem -> item.selectedIcon
                                else -> item.defaultIcon
                            },
                            contentDescription = null,
                            modifier = Modifier.height(35.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = item.text(context),
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = FontFamily(RobotoFont),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                    unselectedTextColor = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}