/*
 * Copyright 2020 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.igdb.api.auth

import com.paulrybitskyi.gamedge.common.api.ApiResult
import com.paulrybitskyi.gamedge.igdb.api.auth.entities.ApiGrantType
import com.paulrybitskyi.gamedge.igdb.api.auth.entities.ApiOauthCredentials

interface AuthEndpoint {
    suspend fun getOauthCredentials(): ApiResult<ApiOauthCredentials>
}

internal class AuthEndpointImpl(
    private val authService: AuthService,
    private val clientId: String,
    private val clientSecret: String
) : AuthEndpoint {

    override suspend fun getOauthCredentials(): ApiResult<ApiOauthCredentials> {
        return authService.getOauthCredentials(
            clientId = clientId,
            clientSecret = clientSecret,
            grantType = ApiGrantType.CLIENT_CREDENTIALS.rawType
        )
    }
}
