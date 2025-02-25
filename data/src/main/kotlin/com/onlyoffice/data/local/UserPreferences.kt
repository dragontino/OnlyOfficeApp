package com.onlyoffice.data.local

import kotlinx.serialization.Serializable

@Serializable
internal data class UserPreferences(
    val portal: String,
    val email: String,
    val token: String,
    val tokenExpirationTime: String? = null
) {
    companion object {
        val Default by lazy {
            UserPreferences(
                portal = "",
                email = "",
                token = ""
            )
        }
    }
}
