package com.onlyoffice.data.di

import android.content.Context
import androidx.datastore.dataStore
import com.onlyoffice.data.local.UserPreferencesSerializer
import com.onlyoffice.data.remote.officeApiModule
import com.onlyoffice.data.repository.AuthRepositoryImpl
import com.onlyoffice.data.repository.DocSpaceRepositoryImpl
import com.onlyoffice.data.repository.PeopleRepositoryImpl
import com.onlyoffice.data.utils.LocalDataStore
import com.onlyoffice.domain.repository.AuthRepository
import com.onlyoffice.domain.repository.DocSpaceRepository
import com.onlyoffice.domain.repository.PeopleRepository
import org.koin.dsl.module

private val Context.userPreferencesDataStore: LocalDataStore by dataStore(
    fileName = "user-preferences",
    serializer = UserPreferencesSerializer
)

val dataModule = module {
    includes(officeApiModule)

    single<AuthRepository> {
        AuthRepositoryImpl(
            api = get(),
            localDataStore = get<Context>().userPreferencesDataStore
        )
    }

    single<DocSpaceRepository> {
        DocSpaceRepositoryImpl(
            api = get(),
            localStore = get<Context>().userPreferencesDataStore
        )
    }

    single<PeopleRepository> {
        PeopleRepositoryImpl(
            api = get(),
            localDataStore = get<Context>().userPreferencesDataStore
        )
    }
}