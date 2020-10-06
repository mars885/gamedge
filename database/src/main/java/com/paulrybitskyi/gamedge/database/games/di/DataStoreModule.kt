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

package com.paulrybitskyi.gamedge.database.games.di

import com.paulrybitskyi.gamedge.commons.data.querying.QueryTimestampProvider
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.games.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.database.commons.di.qualifiers.Database
import com.paulrybitskyi.gamedge.database.commons.utils.JsonConverter
import com.paulrybitskyi.gamedge.database.games.GamesTable
import com.paulrybitskyi.gamedge.database.games.datastore.GameMapper
import com.paulrybitskyi.gamedge.database.games.datastore.GamesDatabaseDataStoreImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
    fun provideGamesLocalDataStore(
        gamesTable: GamesTable,
        dispatcherProvider: DispatcherProvider,
        queryTimestampProvider: QueryTimestampProvider,
        gameMapper: GameMapper
    ): GamesLocalDataStore {
        return GamesDatabaseDataStoreImpl(
            gamesTable = gamesTable,
            dispatcherProvider = dispatcherProvider,
            queryTimestampProvider = queryTimestampProvider,
            gameMapper = gameMapper
        )
    }


    @Provides
    fun provideGameMapper(jsonConverter: JsonConverter): GameMapper {
        return GameMapper(jsonConverter)
    }


    @Provides
    fun provideJsonConverter(@Database moshi: Moshi): JsonConverter {
        return JsonConverter(moshi)
    }


    @Database
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }


}