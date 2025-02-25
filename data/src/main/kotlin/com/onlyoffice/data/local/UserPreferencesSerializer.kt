package com.onlyoffice.data.local

import androidx.datastore.core.Serializer
import com.onlyoffice.data.Crypto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

internal object UserPreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue = UserPreferences.Default

    override suspend fun readFrom(input: InputStream): UserPreferences {
        val encryptedBytes = withContext(Dispatchers.IO) {
            input.use {
                it.readBytes().decodeFromBase64Array()
            }
        }
        val decryptedBytes = Crypto.decrypt(encryptedBytes)
        return Json.decodeFromString(decryptedBytes.decodeToString())
    }

    override suspend fun writeTo(preferences: UserPreferences, output: OutputStream) {
        val preferencesBytes = Json.encodeToString(preferences).toByteArray()
        val encryptedBytes = Crypto.encrypt(preferencesBytes).encodeToBase64Array()
        withContext(Dispatchers.IO) {
            output.use { it.write(encryptedBytes) }
        }
    }

    private fun ByteArray.encodeToBase64Array(): ByteArray {
        return Base64.getEncoder().encode(this)
    }

    private fun ByteArray.decodeFromBase64Array(): ByteArray {
        return Base64.getDecoder().decode(this)
    }
}