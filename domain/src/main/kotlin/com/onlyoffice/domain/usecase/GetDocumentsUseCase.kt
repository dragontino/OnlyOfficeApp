package com.onlyoffice.domain.usecase

import android.util.Log
import com.onlyoffice.domain.model.ProgressListener
import com.onlyoffice.domain.model.docspace.DocumentsData
import com.onlyoffice.domain.repository.DocSpaceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface GetDocumentsUseCase {
    suspend operator fun invoke(
        folderId: Int? = null,
        progressListener: ProgressListener? = null
    ): Result<DocumentsData>
}


internal class GetDocumentsUseCaseImpl(
    private val repository: DocSpaceRepository,
    private val dispatcher: CoroutineDispatcher
) : GetDocumentsUseCase {
    private companion object {
        const val TAG = "GetAllDocumentsUseCase"
    }

    override suspend fun invoke(
        folderId: Int?,
        progressListener: ProgressListener?
    ): Result<DocumentsData> {
        return withContext(dispatcher) {
            val result = when (folderId) {
                null -> repository.getAllDocuments(progressListener)
                else -> repository.getFolderById(folderId, progressListener)
            }

            return@withContext result.onFailure { throwable ->
                Log.e(TAG, throwable.message, throwable)
            }
        }
    }
}