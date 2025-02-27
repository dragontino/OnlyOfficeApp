package com.onlyoffice.app.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
enum class NavDestinations {
    Entry,
    Main;

    val route: String get() = name
}


@Serializable
enum class MainDestinations {
    Documents,
    Rooms,
    Trash,
    Profile;

    val route: String get() = name
}