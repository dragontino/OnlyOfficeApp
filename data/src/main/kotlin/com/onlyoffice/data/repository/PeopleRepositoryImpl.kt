package com.onlyoffice.data.repository

import com.onlyoffice.data.remote.OfficeApi
import com.onlyoffice.data.utils.LocalDataStore
import com.onlyoffice.data.utils.mapToAuthenticatedRequestData
import com.onlyoffice.domain.model.ProgressListener
import com.onlyoffice.domain.model.account.UserInfo
import com.onlyoffice.domain.repository.PeopleRepository

internal class PeopleRepositoryImpl(
    private val api: OfficeApi,
    localDataStore: LocalDataStore
) : PeopleRepository, BaseRepository(localDataStore) {

    override suspend fun getUserInfo(progressListener: ProgressListener?): Result<UserInfo> {
        return runCatching {
            val localStoreData = getLocallySavedData()
                ?: throw Exceptions.userNotAuthenticatedException

            val remoteUserInformation = api.getUserInfo(
                requestData = localStoreData.mapToAuthenticatedRequestData(),
                progressListener = progressListener
            )
            return@runCatching with(remoteUserInformation) {
                UserInfo(
                    id = id.toString(),
                    email = email,
                    displayName = displayName,
                    avatarUrl = "https://${localStoreData.portal}$avatarMax"
                )
            }
        }
    }
}