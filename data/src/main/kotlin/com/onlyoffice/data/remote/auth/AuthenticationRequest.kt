package com.onlyoffice.data.remote.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationRequest(
    val userName: String,
    val password: String
)
