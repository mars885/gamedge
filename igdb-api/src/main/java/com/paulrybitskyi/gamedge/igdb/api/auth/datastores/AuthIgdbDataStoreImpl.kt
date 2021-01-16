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

package com.paulrybitskyi.gamedge.igdb.api.auth.datastores

import com.github.michaelbull.result.mapEither
import com.paulrybitskyi.gamedge.data.auth.datastores.AuthRemoteDataStore
import com.paulrybitskyi.gamedge.data.auth.entities.OauthCredentials
import com.paulrybitskyi.gamedge.data.commons.DataResult
import com.paulrybitskyi.gamedge.igdb.api.auth.AuthEndpoint
import com.paulrybitskyi.gamedge.igdb.api.commons.ErrorMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AuthIgdbDataStoreImpl @Inject constructor(
    private val authEndpoint: AuthEndpoint,
    private val authMapper: AuthMapper,
    private val errorMapper: ErrorMapper
) : AuthRemoteDataStore {


    override suspend fun getOauthCredentials(): DataResult<OauthCredentials> {
        return authEndpoint
            .getOauthCredentials()
            .mapEither(authMapper::mapToDataOauthCredentials, errorMapper::mapToDataError)
    }


}