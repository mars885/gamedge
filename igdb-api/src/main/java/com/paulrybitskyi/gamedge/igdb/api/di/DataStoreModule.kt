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

package com.paulrybitskyi.gamedge.igdb.api.di

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.datastores.GamesServerDataStore
import com.paulrybitskyi.gamedge.igdb.api.IgdbApi
import com.paulrybitskyi.gamedge.igdb.api.datastore.EntityMapper
import com.paulrybitskyi.gamedge.igdb.api.datastore.GamesServerDataStoreImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
internal object DataStoreModule {


    @Singleton
    @Provides
    fun provideGamesServerDataStore(
        igdbApi: IgdbApi,
        entityMapper: EntityMapper,
        dispatcherProvider: DispatcherProvider
    ): GamesServerDataStore {
        return GamesServerDataStoreImpl(
            igdbApi = igdbApi,
            entityMapper = entityMapper,
            dispatcherProvider = dispatcherProvider
        )
    }


    @Provides
    fun provideEntityMapper(): EntityMapper {
        return EntityMapper()
    }


}