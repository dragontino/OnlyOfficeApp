package com.onlyoffice.app.ui.screens.main.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onlyoffice.domain.model.account.UserInfo
import com.onlyoffice.domain.usecase.GetUserInfoUseCase
import com.onlyoffice.domain.usecase.LogoutUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    userInfoUseCase: GetUserInfoUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var loadingProgress: Float? by mutableStateOf(null)
        private set

    private val messageChannel = Channel<String>()
    val messageFlow = messageChannel.receiveAsFlow()

    var userInfo: UserInfo? by mutableStateOf(null)
        private set

    init {
        viewModelScope.launch {
            isLoading = true
            userInfoUseCase {
                loadingProgress = it
            }.onSuccess {
                userInfo = it
            }.onFailure { throwable ->
                throwable.localizedMessage?.ifBlank { "Failed to load user information" }?.let {
                    messageChannel.send(it)
                }
            }
            isLoading = false
            loadingProgress = null
        }
    }


    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            logoutUseCase {
                loadingProgress = it
            }.onSuccess {
                messageChannel.send("Successful logout!")
                onSuccess()
            }.onFailure { throwable ->
                throwable.localizedMessage?.ifBlank { "Failed to log out from account" }?.let {
                    messageChannel.send(it)
                }
            }
            isLoading = false
        }
    }


    fun sendMessage(message: String) {
        messageChannel.trySend(message)
    }
}