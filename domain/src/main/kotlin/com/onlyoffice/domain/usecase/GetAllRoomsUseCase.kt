package com.onlyoffice.domain.usecase

import android.util.Log
import com.onlyoffice.domain.model.ProgressListener
import com.onlyoffice.domain.model.docspace.DocumentsData
import com.onlyoffice.domain.repository.DocSpaceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface GetAllRoomsUseCase {
    suspend operator fun invoke(progressListener: ProgressListener? = null): Result<DocumentsData>
}


internal class GetAllRoomsUseCaseImpl(
    private val repository: DocSpaceRepository,
    private val dispatcher: CoroutineDispatcher
) : GetAllRoomsUseCase {
    private companion object {
        const val TAG = "GetAllRoomsUseCase"
    }

    override suspend fun invoke(progressListener: ((Float) -> Unit)?): Result<DocumentsData> {
        return withContext(dispatcher) {
            repository.getAllRooms(progressListener).onFailure {
                Log.e(TAG, it.message, it)
            }
        }
    }
}