package com.onlyoffice.app.di

import com.onlyoffice.app.ui.screens.entry.EntryViewModel
import com.onlyoffice.app.ui.screens.main.documents.DocumentsViewModel
import com.onlyoffice.app.ui.screens.main.profile.ProfileViewModel
import com.onlyoffice.app.ui.screens.main.rooms.RoomsViewModel
import com.onlyoffice.app.ui.screens.main.trash.TrashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::EntryViewModel)
    viewModelOf(::DocumentsViewModel)
    viewModelOf(::RoomsViewModel)
    viewModelOf(::TrashViewModel)
    viewModelOf(::ProfileViewModel)
}