package com.onlyoffice.domain.model.account

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id: String,
    val email: String?,
    val displayName: String?,
    val avatarUrl: String?,
)