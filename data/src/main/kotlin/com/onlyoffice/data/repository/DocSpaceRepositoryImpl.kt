package com.onlyoffice.data.repository

import com.onlyoffice.data.remote.OfficeApi
import com.onlyoffice.data.remote.model.RemoteDocumentsData
import com.onlyoffice.data.utils.LocalDataStore
import com.onlyoffice.data.utils.mapToAuthenticatedRequestData
import com.onlyoffice.domain.model.ProgressListener
import com.onlyoffice.domain.model.docspace.DocumentsData
import com.onlyoffice.domain.model.docspace.File
import com.onlyoffice.domain.model.docspace.FileType
import com.onlyoffice.domain.repository.DocSpaceRepository

internal class DocSpaceRepositoryImpl(
    private val api: OfficeApi,
    localStore: LocalDataStore
) : DocSpaceRepository, BaseRepository(localStore) {
    override suspend fun getAllDocuments(progressListener: ((Float) -> Unit)?): Result<DocumentsData> {
        return runCatching {
            val remoteDocumentsData = api.getAllDocumentsSection(
                requestData = getLocallySavedData()
                    ?.mapToAuthenticatedRequestData()
                    ?: throw Exceptions.userNotAuthenticatedException,
                progressListener = progressListener
            )
            return@runCatching remoteDocumentsData.mapToDomainDocumentsData()
        }
    }


    override suspend fun getFolderById(
        id: Int,
        progressListener: ProgressListener?
    ): Result<DocumentsData> = runCatching {
        val remoteData = api.getFolderById(
            folderId = id,
            progressListener = progressListener,
            requestData = getLocallySavedData()
                ?.mapToAuthenticatedRequestData()
                ?: throw Exceptions.userNotAuthenticatedException,
        )
        return@runCatching remoteData.mapToDomainDocumentsData()
    }


    override suspend fun getAllRooms(progressListener: ProgressListener?): Result<DocumentsData> {
        return runCatching {
            api.getRoomsSection(
                progressListener = progressListener,
                requestData = getLocallySavedData()
                    ?.mapToAuthenticatedRequestData()
                    ?: throw Exceptions.userNotAuthenticatedException
            ).mapToDomainDocumentsData()
        }
    }


    override suspend fun getTrashFolder(progressListener: ProgressListener?): Result<DocumentsData> {
        return runCatching {
            val remoteData = api.getTrashSection(
                progressListener = progressListener,
                requestData = getLocallySavedData()
                    ?.mapToAuthenticatedRequestData()
                    ?: throw Exceptions.userNotAuthenticatedException
            )
            return@runCatching remoteData.mapToDomainDocumentsData()
        }
    }



    private fun RemoteDocumentsData.mapToDomainDocumentsData() = DocumentsData(
        files = files
            .map {
                File(
                    id = it.id,
                    title = it.title,
                    folderId = it.folderId,
                    type = getFileTypeFromInt(it.fileType, it.fileExt)
                )
            }
            .sortedBy { it.title },
        folders = folders.sortedBy { it.title },
        currentFolder = currentFolder
    )


    private fun getFileTypeFromInt(fileTypeInt: Int, fileExt: String?): FileType? {
        return when (fileTypeInt) {
            5 -> FileType.Spreadsheet(fileExt)
            6 -> FileType.Presentation(fileExt)
            7 -> FileType.Document(fileExt)
            10 -> FileType.Pdf(fileExt)
            else -> null
        }
    }
}