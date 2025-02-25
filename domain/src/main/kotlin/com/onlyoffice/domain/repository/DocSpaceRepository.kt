package com.onlyoffice.domain.repository

import com.onlyoffice.domain.model.ProgressListener
import com.onlyoffice.domain.model.docspace.DocumentsData

interface DocSpaceRepository {
    suspend fun getAllDocuments(progressListener: ProgressListener? = null): Result<DocumentsData>

    suspend fun getFolderById(id: Int, progressListener: ProgressListener? = null): Result<DocumentsData>

    suspend fun getAllRooms(progressListener: ProgressListener? = null): Result<DocumentsData>

    suspend fun getTrashFolder(progressListener: ProgressListener? = null): Result<DocumentsData>
}