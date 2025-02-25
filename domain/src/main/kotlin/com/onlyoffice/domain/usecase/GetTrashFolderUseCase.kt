package com.onlyoffice.domain.usecase

import android.util.Log
import com.onlyoffice.domain.model.ProgressListener
import com.onlyoffice.domain.model.docspace.DocumentsData
import com.onlyoffice.domain.repository.DocSpaceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface GetTrashFolderUseCase {
    suspend operator fun invoke(progressListener: ProgressListener? = null): Result<DocumentsData>
}


internal class GetTrashFolderUseCaseImpl(
    private val repository: DocSpaceRepository,
    private val dispatcher: CoroutineDispatcher
) : GetTrashFolderUseCase {
    private companion object {
        const val TAG = "GetTrashFolderUseCase"
    }

    override suspend fun invoke(progressListener: ProgressListener?): Result<DocumentsData> {
        return withContext(dispatcher) {
            repository.getTrashFolder(progressListener).onFailure {
                Log.e(TAG, it.message, it)
            }
        }
    }
}