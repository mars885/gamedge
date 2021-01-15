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

package com.paulrybitskyi.gamedge.igdb.api.auth.di

import com.paulrybitskyi.gamedge.data.auth.datastores.AuthRemoteDataStore
import com.paulrybitskyi.gamedge.igdb.api.auth.AuthEndpoint
import com.paulrybitskyi.gamedge.igdb.api.auth.datastores.AuthIgdbDataStoreImpl
import com.paulrybitskyi.gamedge.igdb.api.auth.datastores.AuthMapper
import com.paulrybitskyi.gamedge.igdb.api.commons.ErrorMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {


    @Provides
    @Singleton
    fun provideAuthIgdbDataStore(
        authEndpoint: AuthEndpoint,
        authMapper: AuthMapper,
        errorMapper: ErrorMapper
    ): AuthRemoteDataStore {
        return AuthIgdbDataStoreImpl(
            authEndpoint = authEndpoint,
            authMapper = authMapper,
            errorMapper = errorMapper
        )
    }


    @Provides
    fun provideAuthMapper(): AuthMapper {
        return AuthMapper()
    }


}