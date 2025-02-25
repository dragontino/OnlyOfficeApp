package com.onlyoffice.app.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
enum class NavDestinations {
    Entry,
    Main,
    Documents,
    Rooms,
    Trash,
    Profile,
}