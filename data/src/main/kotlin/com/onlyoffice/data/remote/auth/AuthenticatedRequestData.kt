package com.onlyoffice.data.remote.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticatedRequestData(
    val portal: String,
    val token: String
)
