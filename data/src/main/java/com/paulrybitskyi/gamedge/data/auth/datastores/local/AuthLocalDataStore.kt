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

package com.paulrybitskyi.gamedge.data.auth.datastores.local

import androidx.datastore.DataStore
import com.paulrybitskyi.gamedge.core.providers.TimestampProvider
import com.paulrybitskyi.gamedge.data.auth.entities.OauthCredentials
import kotlinx.coroutines.flow.first


interface AuthLocalDataStore {

    suspend fun saveOauthCredentials(oauthCredentials: OauthCredentials)

    suspend fun getOauthCredentials(): OauthCredentials?

    suspend fun isExpired(): Boolean

}


internal class AuthFileDataStore(
    private val protoDataStore: DataStore<ProtoOauthCredentials>,
    private val timestampProvider: TimestampProvider,
    private val mapper: AuthMapper
) : AuthLocalDataStore {


    override suspend fun saveOauthCredentials(oauthCredentials: OauthCredentials) {
        protoDataStore.updateData {
            mapper.mapToProtoOauthCredentials(oauthCredentials)
        }
    }


    override suspend fun getOauthCredentials(): OauthCredentials? {
        // Using Kotlin's takeIf and let because Flow's filter with
        // first/firstOrNull does not seem to be working.
        // When Flow's chain is fixed, consider offloading
        // mapping and filtering operations to a background thread
        // since they currently run on the main thread.

        return protoDataStore.data
            .first()
            .takeIf(ProtoOauthCredentials::isNotEmpty)
            ?.let(mapper::mapToDataOauthCredentials)
    }


    override suspend fun isExpired(): Boolean {
        // Same with Kotlin's takeIf and let.

        return protoDataStore.data
            .first()
            .takeIf(ProtoOauthCredentials::isNotEmpty)
            ?.let { timestampProvider.getUnixTimestamp() >= it.expirationTime }
            ?: true
    }


}