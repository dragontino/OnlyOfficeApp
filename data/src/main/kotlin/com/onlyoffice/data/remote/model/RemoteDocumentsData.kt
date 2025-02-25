package com.onlyoffice.data.remote.model

import com.onlyoffice.domain.model.docspace.Folder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RemoteDocumentsData(
    val folders: List<Folder>,
    val files: List<RemoteFile>,
    @SerialName("current")
    val currentFolder: Folder
)
