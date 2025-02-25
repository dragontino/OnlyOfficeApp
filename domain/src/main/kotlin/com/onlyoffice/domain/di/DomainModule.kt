package com.onlyoffice.domain.di

import com.onlyoffice.domain.usecase.AuthenticateUserUseCase
import com.onlyoffice.domain.usecase.AuthenticateUserUseCaseImpl
import com.onlyoffice.domain.usecase.GetAllRoomsUseCase
import com.onlyoffice.domain.usecase.GetAllRoomsUseCaseImpl
import com.onlyoffice.domain.usecase.GetDocumentsUseCase
import com.onlyoffice.domain.usecase.GetDocumentsUseCaseImpl
import com.onlyoffice.domain.usecase.GetSavedCredentialsUseCase
import com.onlyoffice.domain.usecase.GetSavedCredentialsUseCaseImpl
import com.onlyoffice.domain.usecase.GetTrashFolderUseCase
import com.onlyoffice.domain.usecase.GetTrashFolderUseCaseImpl
import com.onlyoffice.domain.usecase.GetUserInfoUseCase
import com.onlyoffice.domain.usecase.GetUserInfoUseCaseImpl
import com.onlyoffice.domain.usecase.LogoutUseCase
import com.onlyoffice.domain.usecase.LogoutUseCaseImpl
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val domainModule = module {
    single<AuthenticateUserUseCase> {
        AuthenticateUserUseCaseImpl(
            repository = get(),
            dispatcher = Dispatchers.IO
        )
    }

    single<GetSavedCredentialsUseCase> {
        GetSavedCredentialsUseCaseImpl(
            repository = get(),
            dispatcher = Dispatchers.IO
        )
    }

    single<LogoutUseCase> {
        LogoutUseCaseImpl(
            repository = get(),
            dispatcher = Dispatchers.IO
        )
    }

    single<GetUserInfoUseCase> {
        GetUserInfoUseCaseImpl(
            repository = get(),
            dispatcher = Dispatchers.IO
        )
    }

    single<GetDocumentsUseCase> {
        GetDocumentsUseCaseImpl(
            repository = get(),
            dispatcher = Dispatchers.IO
        )
    }

    single<GetAllRoomsUseCase> {
        GetAllRoomsUseCaseImpl(
            repository = get(),
            dispatcher = Dispatchers.IO
        )
    }

    single<GetTrashFolderUseCase> {
        GetTrashFolderUseCaseImpl(
            repository = get(),
            dispatcher = Dispatchers.IO
        )
    }
}