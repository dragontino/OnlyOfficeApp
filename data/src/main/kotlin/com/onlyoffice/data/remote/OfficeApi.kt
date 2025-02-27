package com.onlyoffice.data.remote

import com.onlyoffice.data.remote.auth.AuthResponse
import com.onlyoffice.data.remote.auth.AuthenticatedRequestData
import com.onlyoffice.data.remote.auth.AuthenticationRequest
import com.onlyoffice.data.remote.model.RemoteDocumentsData
import com.onlyoffice.data.remote.model.RemoteUserInformation
import com.onlyoffice.domain.model.ProgressListener
import com.onlyoffice.domain.model.account.AuthCredentials
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.onDownload
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal interface OfficeApi {
    suspend fun authenticateUser(
        authCredentials: AuthCredentials,
        progressListener: ProgressListener? = null
    ): AuthResponse

    suspend fun logout(
        requestData: AuthenticatedRequestData,
        progressListener: ProgressListener? = null
    )

    suspend fun getAllDocumentsSection(
        requestData: AuthenticatedRequestData,
        progressListener: ProgressListener? = null
    ): RemoteDocumentsData

    suspend fun getRoomsSection(
        requestData: AuthenticatedRequestData,
        progressListener: ProgressListener? = null
    ): RemoteDocumentsData

    suspend fun getTrashSection(
        requestData: AuthenticatedRequestData,
        progressListener: ProgressListener? = null
    ): RemoteDocumentsData

    suspend fun getFolderById(
        requestData: AuthenticatedRequestData,
        folderId: Int,
        progressListener: ProgressListener? = null
    ): RemoteDocumentsData

    suspend fun getUserInfo(
        requestData: AuthenticatedRequestData,
        progressListener: ProgressListener? = null
    ): RemoteUserInformation
}


private class OfficeApiImpl : OfficeApi {
    private fun getHttpClient(contentType: ContentType) = HttpClient(Android) {
        install(ContentNegotiation) {
            json(contentType = contentType)
        }
    }


    override suspend fun authenticateUser(
        authCredentials: AuthCredentials,
        progressListener: ProgressListener?
    ): AuthResponse {
        val contentType = ContentType.Application.Json
        val json = Json {
            ignoreUnknownKeys = true
        }

        getHttpClient(contentType).use { client ->
            val response = client.post {
                url {
                    applyBaseUrlFromPortal(authCredentials.portal)
                    appendPathSegments("api", "2.0", "authentication")
                }

                contentType(contentType)
                accept(ContentType.parse("text/json"))
                setBody(
                    body = AuthenticationRequest(
                        userName = authCredentials.email,
                        password = authCredentials.password
                    )
                )
                progressListener?.let { setProgressListener(listener = it) }
            }

            val responseBody = response.bodyAsText()
            val responseJson = json.parseToJsonElement(responseBody) as JsonObject
            when {
                responseJson["status"]?.toString() == "404" -> {
                    throw Exceptions.getPortalNotFoundException(authCredentials.portal)
                }

                responseJson["error"] != null -> {
                    val error = responseJson["error"]!!.let(json::encodeToJsonElement) as JsonObject
                    throw Exception(error["message"]!!.let { json.decodeFromJsonElement<String>(it) })
                }

                else -> return responseJson["response"]
                    ?.let { json.encodeToJsonElement(it) as JsonObject }
                    ?.also {
                        if ("token" !in it)
                            throw Exceptions.getNoAuthTokenException(authCredentials.email)
                    }
                    ?.let {
                        AuthResponse(
                            portal = authCredentials.portal,
                            email = authCredentials.email,
                            token = it["token"]!!.let(json::decodeFromJsonElement),
                            expires = it["expires"]!!.let(json::decodeFromJsonElement)
                        )
                    }
                    ?: throw Exceptions.getUnexpectedResponseException(responseBody)
            }
        }
    }


    override suspend fun logout(
        requestData: AuthenticatedRequestData,
        progressListener: ProgressListener?
    ) {
        val contentType = ContentType.Application.Json
        getHttpClient(contentType).use { client ->
            val response = client.post {
                url {
                    applyBaseUrlFromPortal(requestData.portal)
                    appendPathSegments("api", "2.0", "authentication", "logout")
                }

                bearerAuth(token = requestData.token)
                accept(contentType)
                setBody("")
                progressListener?.let { setProgressListener(listener = it) }
            }

            if (response.status.isSuccess().not()) {
                throw Exception("${response.status.value}. ${response.status.description}")
            }
        }
    }


    override suspend fun getAllDocumentsSection(
        requestData: AuthenticatedRequestData,
        progressListener: ProgressListener?
    ): RemoteDocumentsData {
        return getSpecifiedFileSection(
            "@my", requestData = requestData, progressListener = progressListener
        )
    }


    override suspend fun getRoomsSection(
        requestData: AuthenticatedRequestData,
        progressListener: ((Float) -> Unit)?
    ): RemoteDocumentsData {
        return getSpecifiedFileSection(
            "rooms", requestData = requestData, progressListener = progressListener
        )
    }


    override suspend fun getTrashSection(
        requestData: AuthenticatedRequestData,
        progressListener: ((Float) -> Unit)?
    ): RemoteDocumentsData {
        return getSpecifiedFileSection(
            "@trash", requestData = requestData, progressListener = progressListener
        )
    }


    override suspend fun getFolderById(
        requestData: AuthenticatedRequestData,
        folderId: Int,
        progressListener: ProgressListener?
    ): RemoteDocumentsData {
        return getSpecifiedFileSection(
            folderId.toString(), requestData = requestData, progressListener = progressListener
        )
    }


    override suspend fun getUserInfo(
        requestData: AuthenticatedRequestData,
        progressListener: ProgressListener?
    ): RemoteUserInformation {
        val contentType = ContentType.Application.Json
        val json = Json { ignoreUnknownKeys = true }

        getHttpClient(contentType).use { client ->
            val response = client.get {
                url {
                    applyBaseUrlFromPortal(requestData.portal)
                    appendPathSegments("api", "2.0", "people", "@self")
                }
                accept(contentType)
                bearerAuth(requestData.token)
                progressListener?.let { setProgressListener(onUpload = false, listener = it) }
            }

            if (response.status.isSuccess().not()) {
                throw Exception("${response.status.value}. ${response.status.description}")
            }

            val responseBody = response.bodyAsText()
            val responseJson = json.parseToJsonElement(responseBody) as JsonObject
            return responseJson["response"]
                ?.let { json.decodeFromJsonElement(it) }
                ?: throw Exceptions.getUnexpectedResponseException(responseBody)
        }
    }



    private fun URLBuilder.applyBaseUrlFromPortal(portal: String) {
        protocol = URLProtocol.HTTPS
        host = portal.removePrefix("http://").removePrefix("https://")
    }


    private inline fun HttpRequestBuilder.setProgressListener(
        onUpload: Boolean = true,
        crossinline listener: (Float) -> Unit
    ) {
        val listener = object : io.ktor.client.content.ProgressListener {
            override suspend fun onProgress(bytesSentTotal: Long, contentLength: Long?) {
                if (contentLength != null) {
                    listener(bytesSentTotal.toFloat() / contentLength)
                }
            }
        }
        when {
            onUpload -> onUpload(listener)
            else -> onDownload(listener)
        }
    }


    private suspend inline fun <reified T> getSpecifiedFileSection(
        vararg lastPathSections: String,
        requestData: AuthenticatedRequestData,
        noinline progressListener: ProgressListener?
    ): T {
        val contentType = ContentType.Application.Json
        val json = Json { ignoreUnknownKeys = true }

        getHttpClient(contentType).use { client ->
            val response = client.get {
                url {
                    applyBaseUrlFromPortal(requestData.portal)
                    appendPathSegments("api", "2.0", "files")
                    appendPathSegments(components = lastPathSections)
                }
                bearerAuth(token = requestData.token)
                accept(contentType)
                progressListener?.let { setProgressListener(onUpload = false, listener = it) }
            }

            if (response.status.isSuccess().not()) {
                throw Exception("${response.status.value}. ${response.status.description}")
            }

            val responseBody = response.bodyAsText()
            val responseJson = json.parseToJsonElement(responseBody) as JsonObject
            return responseJson["response"]
                ?.let(json::decodeFromJsonElement)
                ?: throw Exceptions.getUnexpectedResponseException(responseBody)
        }
    }


    private object Exceptions {
        fun getUnexpectedResponseException(response: String) =
            SerializationException("Got an unexpected response: $response")

        fun getPortalNotFoundException(portal: String) = Exception("Portal $portal is not found")

        fun getNoAuthTokenException(email: String) =
            Exception("There is no token related with email $email")
    }
}




internal val officeApiModule = module {
    singleOf<OfficeApi>(::OfficeApiImpl)
}