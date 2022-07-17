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

package com.paulrybitskyi.gamedge.feature.settings.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.paulrybitskyi.gamedge.feature.settings.data.Constants
import com.paulrybitskyi.gamedge.feature.settings.data.datastores.ProtoSettings
import com.paulrybitskyi.gamedge.feature.settings.data.datastores.ProtoSettingsSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

private val Context.settingsProtoDataStore by dataStore(
    fileName = Constants.SETTINGS_PREFERENCES_DATA_STORE_NAME,
    serializer = ProtoSettingsSerializer,
)

@Module
@InstallIn(SingletonComponent::class)
internal object CoreModule {

    @Provides
    fun provideSettingsProtoDataStore(
        @ApplicationContext context: Context,
    ): DataStore<ProtoSettings> {
        return context.settingsProtoDataStore
    }
}
