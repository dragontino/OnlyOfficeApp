package com.onlyoffice.domain.repository

import com.onlyoffice.domain.model.ProgressListener
import com.onlyoffice.domain.model.account.AuthCredentials
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun checkUserIsAlreadyAuthenticated(): Result<Boolean>

    fun getSavedCredentials(): Flow<AuthCredentials>

    suspend fun authenticateUser(
        credentials: AuthCredentials,
        progressListener: ProgressListener? = null
    ): Result<Unit>

    suspend fun logout(progressListener: ProgressListener? = null): Result<Unit>
}