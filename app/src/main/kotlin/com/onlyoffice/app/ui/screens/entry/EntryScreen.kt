package com.onlyoffice.app.ui.screens.entry

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.onlyoffice.app.R
import com.onlyoffice.app.util.FullScreenContentWithLoading
import com.onlyoffice.app.util.FullScreenProgressIndicator
import org.koin.androidx.compose.koinViewModel

@Composable
fun EntryScreen(
    navigateToMainScreen: () -> Unit,
    showSnackbar: suspend (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EntryViewModel = koinViewModel()
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
    FullScreenContentWithLoading(
        loadingState = remember {
            derivedStateOf { viewModel.isLoading }
        },
        loadingContent = { FullScreenProgressIndicator(animatedLoadingProgress) },
        modifier = modifier.imePadding()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.entry_title),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 64.dp)
                    .fillMaxWidth()
            )

            TextField(
                value = viewModel.state.portalName,
                onValueChange = { portal ->
                    if (portal.all { it.isLetter() || it.isDigit() }) {
                        viewModel.updateState {
                            portalName = portal.lowercase()
                        }
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Language,
                        contentDescription = null
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.portal_name_placeholder),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                suffix = {
                    Text(
                        text = ".${EntryViewModel.DOMAIN_NAME}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                shape = MaterialTheme.shapes.small,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = .7f
                    ),
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = .7f
                    ),
                    focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                    focusedSuffixColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedSuffixColor = MaterialTheme.colorScheme.onSurface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )


            TextField(
                value = viewModel.state.email,
                onValueChange = {
                    viewModel.updateState { email = it }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.PersonOutline,
                        contentDescription = null
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.email_placeholder),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email
                ),
                shape = MaterialTheme.shapes.small,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = .7f
                    ),
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = .7f
                    ),
                    focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )


            val passwordIsVisible = rememberSaveable { mutableStateOf(false) }
            TextField(
                value = viewModel.state.password,
                onValueChange = {
                    viewModel.updateState { password = it }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Password,
                        contentDescription = null
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.password_placeholder),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { passwordIsVisible.value = passwordIsVisible.value.not() }
                    ) {
                        Icon(
                            imageVector = when {
                                passwordIsVisible.value -> Icons.Outlined.VisibilityOff
                                else -> Icons.Outlined.Visibility
                            },
                            contentDescription = null
                        )
                    }
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                visualTransformation = when {
                    passwordIsVisible.value -> VisualTransformation.None
                    else -> PasswordVisualTransformation()
                },
                shape = MaterialTheme.shapes.small,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = .7f
                    ),
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = .7f
                    ),
                    focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            TextButton(
                onClick = {
                    viewModel.authenticate(onSuccess = navigateToMainScreen)
                },
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.textButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.login),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}