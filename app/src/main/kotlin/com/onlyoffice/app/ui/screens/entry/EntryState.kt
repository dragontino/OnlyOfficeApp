package com.onlyoffice.app.ui.screens.entry

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class EntryState(
    val portalName: String = "",
    val email: String = "",
    val password: String = ""
)


data class MutableEntryState(
    var portalName: String = "",
    var email: String = "",
    var password: String = ""
)