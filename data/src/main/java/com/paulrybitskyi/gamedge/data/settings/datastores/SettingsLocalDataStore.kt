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

package com.paulrybitskyi.gamedge.data.settings.datastores

import androidx.datastore.core.DataStore
import com.paulrybitskyi.gamedge.data.settings.DataSettings
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

interface SettingsLocalDataStore {
    suspend fun saveSettings(settings: DataSettings)
    fun observeSettings(): Flow<DataSettings>
}

@Singleton
@BindType
internal class SettingsFileDataStore @Inject constructor(
    private val protoDataStore: DataStore<ProtoSettings>,
    private val protoMapper: ProtoSettingsMapper,
) : SettingsLocalDataStore {

    override suspend fun saveSettings(settings: DataSettings) {
        protoDataStore.updateData {
            protoMapper.mapToProtoSettings(settings)
        }
    }

    override fun observeSettings(): Flow<DataSettings> {
        return protoDataStore.data
            .map(protoMapper::mapToDataSettings)
    }
}
