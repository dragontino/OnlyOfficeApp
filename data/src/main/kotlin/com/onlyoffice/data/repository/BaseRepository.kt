package com.onlyoffice.data.repository

import android.security.keystore.UserNotAuthenticatedException
import com.onlyoffice.data.local.UserPreferences
import com.onlyoffice.data.utils.LocalDataStore
import kotlinx.coroutines.flow.first

internal abstract class BaseRepository(protected val localDataStore: LocalDataStore) {
    protected object Exceptions {
        val userNotAuthenticatedException = UserNotAuthenticatedException("User is not authenticated")
    }

    protected suspend fun getLocallySavedData(): UserPreferences? {
        return localDataStore.data.first().takeIf { it != UserPreferences.Default }
    }
}