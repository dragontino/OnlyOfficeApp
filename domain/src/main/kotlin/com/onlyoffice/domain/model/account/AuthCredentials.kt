package com.onlyoffice.domain.model.account

import kotlinx.serialization.Serializable

@Serializable
data class AuthCredentials(
    val portal: String,
    val email: String,
    val password: String
)
