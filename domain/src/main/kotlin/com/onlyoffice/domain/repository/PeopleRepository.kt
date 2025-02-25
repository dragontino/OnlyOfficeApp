package com.onlyoffice.domain.repository

import com.onlyoffice.domain.model.ProgressListener
import com.onlyoffice.domain.model.account.UserInfo

interface PeopleRepository {
    suspend fun getUserInfo(progressListener: ProgressListener? = null): Result<UserInfo>
}