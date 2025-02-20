/*
 * Copyright 2021 Paul Rybitskyi, oss@paulrybitskyi.com
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
import com.paulrybitskyi.gamedge.igdb.api.auth.Authorizer
import com.paulrybitskyi.gamedge.igdb.api.auth.entities.ApiAuthorizationType
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

internal class AuthorizationInterceptor(
    private val credentialsStore: CredentialsStore,
    private val authorizer: Authorizer,
    private val clientId: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val authorizationHeader = authorizer.buildAuthorizationHeader(
            type = ApiAuthorizationType.BEARER,
            token = getAccessToken(),
        )
        val authorizedRequest = chain.request()
            .newBuilder()
            .addHeader(ApiHeaders.CLIENT_ID, clientId)
            .addHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)
            .build()

        chain.proceed(authorizedRequest)
    }

    private suspend fun getAccessToken(): String {
        return credentialsStore.getLocalOauthCredentials()
            ?.accessToken
            .orEmpty()
    }
}
