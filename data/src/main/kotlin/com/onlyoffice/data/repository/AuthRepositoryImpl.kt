package com.onlyoffice.data.repository

import com.onlyoffice.data.local.UserPreferences
import com.onlyoffice.data.remote.OfficeApi
import com.onlyoffice.data.remote.auth.AuthResponse
import com.onlyoffice.data.utils.LocalDataStore
import com.onlyoffice.data.utils.mapToAuthenticatedRequestData
import com.onlyoffice.data.utils.mapToUserPreferences
import com.onlyoffice.domain.model.ProgressListener
import com.onlyoffice.domain.model.account.AuthCredentials
import com.onlyoffice.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal class AuthRepositoryImpl(
    private val api: OfficeApi,
    localDataStore: LocalDataStore
) : AuthRepository, BaseRepository(localDataStore) {

    override suspend fun checkUserIsAlreadyAuthenticated(): Result<Boolean> {
        return runCatching {
            val savedAuthenticationData = getLocallySavedData() ?: return@runCatching false
            return@runCatching with(savedAuthenticationData) {
                tokenExpirationTime == null || checkTokenIsExpired(tokenExpirationTime)
            }
        }
    }

    override suspend fun authenticateUser(
        credentials: AuthCredentials,
        progressListener: ProgressListener?
    ): Result<Unit> = runCatching {
        val response = api.authenticateUser(credentials, progressListener)
        saveAuthDataToLocalStore(response)
        return Result.success(Unit)
    }

    override suspend fun logout(progressListener: ProgressListener?): Result<Unit> {
        return runCatching {
            api.logout(
                requestData = getLocallySavedData()
                    ?.mapToAuthenticatedRequestData()
                    ?: return Result.success(Unit),
                progressListener = progressListener
            )
            clearSavedData()
        }
    }

    override fun getSavedCredentials(): Flow<AuthCredentials> {
        return localDataStore.data.mapNotNull { userPreferences ->
            userPreferences?.let {
                AuthCredentials(portal = it.portal, email = it.email, password = "")
            }
        }
    }

    private suspend fun saveAuthDataToLocalStore(authResponse: AuthResponse) {
        localDataStore.updateData { authResponse.mapToUserPreferences() }
    }

    private suspend fun clearSavedData() {
        localDataStore.updateData { UserPreferences.Default }
    }

    private fun checkTokenIsExpired(tokenExpirationTime: String): Boolean {
        val now = LocalDateTime.now()

        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val tokenExpirationDateTime = LocalDateTime.parse(tokenExpirationTime, formatter)
        return now < tokenExpirationDateTime
    }
}