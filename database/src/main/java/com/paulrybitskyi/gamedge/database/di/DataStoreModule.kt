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

package com.paulrybitskyi.gamedge.database.di

import com.paulrybitskyi.gamedge.commons.data.querying.QueryTimestampProvider
import com.paulrybitskyi.gamedge.data.datastores.GamesDatabaseDataStore
import com.paulrybitskyi.gamedge.database.datastore.EntityMapper
import com.paulrybitskyi.gamedge.database.datastore.GamesDatabaseDataStoreImpl
import com.paulrybitskyi.gamedge.database.di.qualifiers.Database
import com.paulrybitskyi.gamedge.database.tables.GamesTable
import com.paulrybitskyi.gamedge.database.utils.JsonConverter
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
    fun provideGamesDatabaseDataStore(
        gamesTable: GamesTable,
        queryTimestampProvider: QueryTimestampProvider,
        entityMapper: EntityMapper
    ): GamesDatabaseDataStore {
        return GamesDatabaseDataStoreImpl(
            gamesTable = gamesTable,
            queryTimestampProvider = queryTimestampProvider,
            entityMapper = entityMapper
        )
    }


    @Provides
    fun provideEntityMapper(jsonConverter: JsonConverter): EntityMapper {
        return EntityMapper(jsonConverter)
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