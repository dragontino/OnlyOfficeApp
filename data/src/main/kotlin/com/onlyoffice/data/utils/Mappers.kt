package com.onlyoffice.data.utils

import com.onlyoffice.data.local.UserPreferences
import com.onlyoffice.data.remote.auth.AuthResponse
import com.onlyoffice.data.remote.auth.AuthenticatedRequestData

internal fun UserPreferences.mapToAuthenticatedRequestData() = AuthenticatedRequestData(
    portal = portal,
    token = token
)


internal fun AuthResponse.mapToUserPreferences() = UserPreferences(
    portal = portal,
    email = email,
    token = token,
    tokenExpirationTime = expires
)