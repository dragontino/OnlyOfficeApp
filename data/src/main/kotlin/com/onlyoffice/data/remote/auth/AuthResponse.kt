package com.onlyoffice.data.remote.auth

import kotlinx.serialization.Serializable

@Serializable
internal data class AuthResponse(
    val portal: String,
    val email: String,
    val token: String,
    val expires: String
)