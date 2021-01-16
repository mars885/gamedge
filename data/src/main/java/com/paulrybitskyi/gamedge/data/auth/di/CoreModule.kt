/*
 * Copyright 2021 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

package com.paulrybitskyi.gamedge.data.auth.di

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import com.paulrybitskyi.gamedge.data.auth.Constants
import com.paulrybitskyi.gamedge.data.auth.datastores.local.ProtoOauthCredentials
import com.paulrybitskyi.gamedge.data.auth.datastores.local.ProtoOauthCredentialsSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object CoreModule {


    @Provides
    fun provideAuthProtoDataStore(
        @ApplicationContext context: Context
    ): DataStore<ProtoOauthCredentials> {
        return context.createDataStore(
            fileName = Constants.AUTH_PREFERENCES_DATA_STORE_NAME,
            serializer = ProtoOauthCredentialsSerializer
        )
    }


}