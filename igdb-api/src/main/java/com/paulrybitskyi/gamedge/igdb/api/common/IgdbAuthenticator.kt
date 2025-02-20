/*
 * Copyright 2022 Paul Rybitskyi, oss@paulrybitskyi.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.paulrybitskyi.gamedge.igdb.api.common

import com.paulrybitskyi.gamedge.common.api.HttpHeaders
import com.paulrybitskyi.gamedge.igdb.api.auth.AuthHeaderParser
import com.paulrybitskyi.gamedge.igdb.api.auth.Authorizer
import com.paulrybitskyi.gamedge.igdb.api.auth.entities.ApiAuthorizationType
import com.paulrybitskyi.gamedge.igdb.api.auth.entities.ApiOauthCredentials
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

private const val MAX_AUTH_RESPONSE_COUNT = 3

@BindType
internal class IgdbAuthenticator @Inject constructor(
    private val credentialsStore: CredentialsStore,
    private val authHeaderParser: AuthHeaderParser,
    private val authorizer: Authorizer,
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.responseCount >= MAX_AUTH_RESPONSE_COUNT) {
            return null
        }

        return runBlocking {
            mutex.withLock {
                val request = response.request
                val lastSavedAccessToken = credentialsStore.getLocalOauthCredentials()?.accessToken
                val requestAccessToken = request.getAccessToken()

                if ((lastSavedAccessToken != null) && (requestAccessToken != lastSavedAccessToken)) {
                    request.buildNewRequest(lastSavedAccessToken)
                } else {
                    refreshCredentials()?.let { credentials ->
                        request.buildNewRequest(credentials.accessToken)
                    }
                }
            }
        }
    }

    private fun Request.getAccessToken(): String? {
        return header(HttpHeaders.AUTHORIZATION)
            ?.let(authHeaderParser::parse)
            ?.token
    }

    private suspend fun refreshCredentials(): ApiOauthCredentials? {
        return credentialsStore.getRemoteOauthCredentials()
            ?.also { credentials -> credentialsStore.saveOauthCredentials(credentials) }
    }

    private fun Request.buildNewRequest(newAccessToken: String): Request {
        return newBuilder()
            .header(
                name = HttpHeaders.AUTHORIZATION,
                value = authorizer.buildAuthorizationHeader(
                    type = ApiAuthorizationType.BEARER,
                    token = newAccessToken,
                ),
            )
            .build()
    }
}
