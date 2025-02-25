package com.onlyoffice.data.remote.model

import com.onlyoffice.data.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class RemoteUserInformation(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val email: String?,
    val displayName: String?,
    val avatarMax: String?
)
