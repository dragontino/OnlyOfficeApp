package com.onlyoffice.domain.usecase

import android.util.Log
import com.onlyoffice.domain.model.ProgressListener
import com.onlyoffice.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface LogoutUseCase {
    suspend operator fun invoke(progressListener: ProgressListener? = null): Result<Unit>
}


internal class LogoutUseCaseImpl(
    private val repository: AuthRepository,
    private val dispatcher: CoroutineDispatcher
) : LogoutUseCase {
    private companion object {
        const val TAG = "LogoutUseCase"
    }

    override suspend fun invoke(progressListener: ProgressListener?): Result<Unit> {
        return withContext(dispatcher) {
            repository.logout(progressListener).onFailure {
                Log.e(TAG, it.message, it)
            }
        }
    }
}