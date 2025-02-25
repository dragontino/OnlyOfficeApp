package com.onlyoffice.domain.usecase

import android.util.Log
import com.onlyoffice.domain.model.account.AuthCredentials
import com.onlyoffice.domain.model.ProgressListener
import com.onlyoffice.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface AuthenticateUserUseCase {
    suspend operator fun invoke(
        credentials: AuthCredentials,
        progressListener: ProgressListener? = null
    ): Result<Unit>
}


internal class AuthenticateUserUseCaseImpl(
    private val repository: AuthRepository,
    private val dispatcher: CoroutineDispatcher
) : AuthenticateUserUseCase {
    private companion object {
        const val TAG = "AuthenticateUserUseCase"
    }


    override suspend fun invoke(
        credentials: AuthCredentials,
        progressListener: ProgressListener?
    ): Result<Unit> {
        return withContext(dispatcher) {
            repository.authenticateUser(credentials, progressListener).onFailure {
                Log.e(TAG, it.message, it)
            }
        }
    }
}