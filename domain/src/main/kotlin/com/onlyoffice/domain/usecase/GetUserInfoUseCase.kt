package com.onlyoffice.domain.usecase

import android.util.Log
import com.onlyoffice.domain.model.ProgressListener
import com.onlyoffice.domain.model.account.UserInfo
import com.onlyoffice.domain.repository.PeopleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

interface GetUserInfoUseCase {
    suspend operator fun invoke(progressListener: ProgressListener? = null): Result<UserInfo>
}

internal class GetUserInfoUseCaseImpl(
    private val repository: PeopleRepository,
    private val dispatcher: CoroutineDispatcher
) : GetUserInfoUseCase {
    private companion object {
        const val TAG = "GetUserInfoUseCase"
    }

    override suspend fun invoke(progressListener: ProgressListener?): Result<UserInfo> {
        return withContext(dispatcher) {
            repository.getUserInfo(progressListener).onFailure {
                Log.e(TAG, it.message, it)
            }
        }
    }
}