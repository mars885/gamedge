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

package com.paulrybitskyi.gamedge.common.data.auth.datastores.local.file

import androidx.datastore.core.DataStore
import com.paulrybitskyi.gamedge.common.data.auth.datastores.local.AuthLocalDataStore
import com.paulrybitskyi.gamedge.common.data.auth.entities.OauthCredentials
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
internal class AuthFileDataStore @Inject constructor(
    private val protoDataStore: DataStore<ProtoOauthCredentials>,
    private val protoAuthMapper: ProtoAuthMapper,
) : AuthLocalDataStore {

    override suspend fun saveOauthCredentials(oauthCredentials: OauthCredentials) {
        protoDataStore.updateData {
            protoAuthMapper.mapToProtoOauthCredentials(oauthCredentials)
        }
    }

    override suspend fun getOauthCredentials(): OauthCredentials? {
        // Using Kotlin's takeIf and let because Flow's filter with
        // first/firstOrNull does not seem to be working.
        // When Flow's chain is fixed, consider offloading
        // mapping and filtering operations to a background thread
        // since they currently run on the main thread.

        return protoDataStore.data
            .firstOrNull()
            ?.takeIf(ProtoOauthCredentials::isNotEmpty)
            ?.let(protoAuthMapper::mapToDataOauthCredentials)
    }
}
