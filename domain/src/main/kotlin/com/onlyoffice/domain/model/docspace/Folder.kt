package com.onlyoffice.domain.model.docspace

import kotlinx.serialization.Serializable

@Serializable
data class Folder(
    val id: Int,
    val title: String?,
    val parentId: Int,
    val rootFolderId: Int,
    val filesCount: Int,
    val foldersCount: Int
) {
    fun isRootFolder(): Boolean {
        return rootFolderId == id || parentId == 0
    }
}
