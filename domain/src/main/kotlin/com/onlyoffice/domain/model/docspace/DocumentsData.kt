package com.onlyoffice.domain.model.docspace

import kotlinx.serialization.Serializable

@Serializable
data class DocumentsData(
    val files: List<File>,
    val folders: List<Folder>,
    val currentFolder: Folder
)
