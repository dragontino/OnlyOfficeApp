package com.onlyoffice.domain.model.docspace

import kotlinx.serialization.Serializable

@Serializable
data class File(
    val id: Int,
    val title: String?,
    val folderId: Int,
    val type: FileType?
)
