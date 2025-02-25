package com.onlyoffice.app.ui.screens.main.trash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onlyoffice.domain.model.docspace.DocumentsData
import com.onlyoffice.domain.usecase.GetTrashFolderUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TrashViewModel(useCase: GetTrashFolderUseCase) : ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    private val messageChannel = Channel<String>()
    val messageFlow = messageChannel.receiveAsFlow()

    var loadingProgress: Float? by mutableStateOf(null)
        private set

    var documentsData: DocumentsData? by mutableStateOf(null)

    init {
        viewModelScope.launch {
            isLoading = true
            useCase { loadingProgress = it }
                .onSuccess { documentsData = it }
                .onFailure { throwable ->
                    throwable.localizedMessage?.let { messageChannel.send(it) }
                }
            isLoading = false
            loadingProgress = null
        }
    }
}