package com.onlyoffice.domain.usecase

import android.util.Log
import com.onlyoffice.domain.model.account.AuthCredentials
import com.onlyoffice.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

interface GetSavedCredentialsUseCase {
    operator fun invoke(): Flow<AuthCredentials>
}


internal class GetSavedCredentialsUseCaseImpl(
    private val repository: AuthRepository,
    private val dispatcher: CoroutineDispatcher
) : GetSavedCredentialsUseCase {
    private companion object {
        const val TAG = "GetSavedCredentialsUseCase"
    }

    override fun invoke(): Flow<AuthCredentials> {
        return repository
            .getSavedCredentials()
            .flowOn(dispatcher)
            .catch {
                Log.e(TAG, it.message, it)
                throw it
            }
    }
}