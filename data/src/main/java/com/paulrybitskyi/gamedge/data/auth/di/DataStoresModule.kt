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

package com.paulrybitskyi.gamedge.data.auth.di

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import com.paulrybitskyi.gamedge.core.providers.TimestampProvider
import com.paulrybitskyi.gamedge.data.auth.Constants
import com.paulrybitskyi.gamedge.data.auth.datastores.AuthRemoteDataStore
import com.paulrybitskyi.gamedge.data.auth.datastores.commons.AuthDataStores
import com.paulrybitskyi.gamedge.data.auth.datastores.local.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
internal object DataStoresModule {


    @Singleton
    @Provides
    fun provideAuthFileDataStore(
        protoDataStore: DataStore<ProtoOauthCredentials>,
        timestampProvider: TimestampProvider,
        mapper: AuthMapper
    ): AuthLocalDataStore {
        return AuthFileDataStore(
            protoDataStore = protoDataStore,
            timestampProvider = timestampProvider,
            mapper = mapper
        )
    }


    @Provides
    fun provideAuthProtoDataStore(
        @ApplicationContext context: Context
    ): DataStore<ProtoOauthCredentials> {
        return context.createDataStore(
            fileName = Constants.AUTH_PREFERENCES_DATA_STORE_NAME,
            serializer = ProtoOauthCredentialsSerializer
        )
    }


    @Provides
    fun provideAuthMapper(authExpiryTimeCalculator: AuthExpiryTimeCalculator): AuthMapper {
        return AuthMapper(authExpiryTimeCalculator)
    }


    @Provides
    fun provideAuthExpiryTimeCalculator(
        timestampProvider: TimestampProvider
    ): AuthExpiryTimeCalculator {
        return AuthExpiryTimeCalculator(timestampProvider)
    }


    @Provides
    fun provideAuthDataStores(
        localDataStore: AuthLocalDataStore,
        remoteDataStore: AuthRemoteDataStore
    ): AuthDataStores {
        return AuthDataStores(
            local = localDataStore,
            remote = remoteDataStore
        )
    }


}