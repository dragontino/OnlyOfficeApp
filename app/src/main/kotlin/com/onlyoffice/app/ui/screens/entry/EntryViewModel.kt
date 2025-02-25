package com.onlyoffice.app.ui.screens.entry

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onlyoffice.app.util.isEmailValid
import com.onlyoffice.domain.model.account.AuthCredentials
import com.onlyoffice.domain.usecase.AuthenticateUserUseCase
import com.onlyoffice.domain.usecase.GetSavedCredentialsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class EntryViewModel(
    getSavedCredentialsUseCase: GetSavedCredentialsUseCase,
    private val authenticateUserUseCase: AuthenticateUserUseCase
) : ViewModel() {

    companion object {
        const val DOMAIN_NAME = "onlyoffice.com"
    }

    var isLoading by mutableStateOf(false)
        private set

    var loadingProgress: Float? by mutableStateOf(null)
        private set

    var state by mutableStateOf(EntryState())
        private set

    private val messageChannel = Channel<String>()
    val messageFlow = messageChannel.receiveAsFlow()

    init {
        getSavedCredentialsUseCase.invoke()
            .onEach { credentials ->
                state = EntryState(
                    portalName = credentials.portal.split('.')[0],
                    email = credentials.email,
                    password = credentials.password.ifBlank { state.password }
                )
            }
            .launchIn(viewModelScope)
    }


    fun updateState(block: MutableEntryState.() -> Unit) {
        state = state
            .toMutableEntryState()
            .apply(block)
            .toEntryState()
    }


    fun authenticate(onSuccess: (() -> Unit)? = null) {
        if (state.email.isEmailValid().not()) {
            messageChannel.trySend(
                "Email address ${state.email} is not correct! Please, enter a correct address"
            )
            return
        }

        viewModelScope.launch {
            isLoading = true

            val credentials = AuthCredentials(
                portal = "${state.portalName}.$DOMAIN_NAME",
                email = state.email,
                password = state.password
            )

            authenticateUserUseCase.invoke(
                credentials = credentials,
                progressListener = { loadingProgress = it }
            ).onSuccess {
                messageChannel.send("Login success!")
                onSuccess?.invoke()
            }.onFailure { throwable ->
                throwable.localizedMessage
                    ?.takeIf { it.isNotBlank() }
                    ?.let { messageChannel.send(it) }
            }

            isLoading = false
            loadingProgress = null
        }
    }


    private fun EntryState.toMutableEntryState() = MutableEntryState(
        portalName = portalName,
        email = email,
        password = password
    )

    private fun MutableEntryState.toEntryState() = EntryState(
        portalName = portalName,
        email = email,
        password = password
    )
}