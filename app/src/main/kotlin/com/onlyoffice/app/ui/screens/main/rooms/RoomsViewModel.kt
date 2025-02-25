package com.onlyoffice.app.ui.screens.main.rooms

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onlyoffice.domain.model.docspace.DocumentsData
import com.onlyoffice.domain.model.docspace.File
import com.onlyoffice.domain.model.docspace.Folder
import com.onlyoffice.domain.usecase.GetAllRoomsUseCase
import com.onlyoffice.domain.usecase.GetDocumentsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RoomsViewModel(
    getRoomsUseCase: GetAllRoomsUseCase,
    private val getDocumentsUseCase: GetDocumentsUseCase
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var loadingProgress: Float? by mutableStateOf(null)
        private set

    private val messageChannel = Channel<String>()
    val messageFlow = messageChannel.receiveAsFlow()

    var roomsData: DocumentsData? by mutableStateOf(null)
        private set

    init {
        viewModelScope.launch {
            isLoading = true
            getRoomsUseCase { loadingProgress = it }
                .onSuccess { roomsData = it }
                .onFailure { throwable ->
                throwable.localizedMessage
                    ?.takeIf { it.isNotBlank() }
                    ?.let { messageChannel.send(it) }
            }
            isLoading = false
            loadingProgress = null
        }
    }


    fun openChildFolder(folder: Folder) {
        if (folder.id != roomsData?.currentFolder?.id) {
            viewModelScope.launch {
                openFolder(folder.id)
            }
        }
    }

    fun openParentFolder() {
        val currentFolder = roomsData?.currentFolder ?: return
        if (currentFolder.isRootFolder().not()) {
            viewModelScope.launch {
                openFolder(currentFolder.parentId)
            }
        }
    }

    fun openFile(file: File) {
        messageChannel.trySend("Trying open file ${file.title}")
    }


    private suspend fun openFolder(id: Int) {
        isLoading = true
        getDocumentsUseCase(folderId = id, progressListener = { loadingProgress = it })
            .onSuccess { roomsData = it }
            .onFailure { throwable ->
                throwable.localizedMessage?.let { messageChannel.send(it) }
            }
        isLoading = false
        loadingProgress = null
    }
}