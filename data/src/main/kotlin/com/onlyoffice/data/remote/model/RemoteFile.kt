package com.onlyoffice.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteFile(
    val id: Int,
    val title: String?,
    val folderId: Int,
    val fileType: Int,
    @SerialName("fileExst")
    val fileExt: String?
)
