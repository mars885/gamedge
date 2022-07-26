/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.common.data.auth.datastores.igdb

import com.github.michaelbull.result.get
import com.paulrybitskyi.gamedge.common.domain.auth.datastores.AuthLocalDataStore
import com.paulrybitskyi.gamedge.common.domain.auth.datastores.AuthRemoteDataStore
import com.paulrybitskyi.gamedge.igdb.api.auth.entities.ApiOauthCredentials
import com.paulrybitskyi.gamedge.igdb.api.common.CredentialsStore
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
internal class CredentialsStoreImpl @Inject constructor(
    private val authLocalDataStore: AuthLocalDataStore,
    private val authRemoteDataStore: AuthRemoteDataStore,
    private val igdbAuthMapper: IgdbAuthMapper,
) : CredentialsStore {

    override suspend fun saveOauthCredentials(oauthCredentials: ApiOauthCredentials) {
        authLocalDataStore.saveOauthCredentials(
            igdbAuthMapper.mapToDomainOauthCredentials(oauthCredentials),
        )
    }

    override suspend fun getLocalOauthCredentials(): ApiOauthCredentials? {
        return authLocalDataStore.getOauthCredentials()
            ?.let(igdbAuthMapper::mapToApiOauthCredentials)
    }

    override suspend fun getRemoteOauthCredentials(): ApiOauthCredentials? {
        return authRemoteDataStore.getOauthCredentials()
            .get()
            ?.let(igdbAuthMapper::mapToApiOauthCredentials)
    }
}
