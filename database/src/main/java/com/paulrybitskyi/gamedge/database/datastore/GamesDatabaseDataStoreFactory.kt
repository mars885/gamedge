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

package com.paulrybitskyi.gamedge.database.datastore

import android.content.Context
import com.paulrybitskyi.gamedge.commons.data.querying.QueryTimestampProvider
import com.paulrybitskyi.gamedge.commons.data.querying.QueryTimestampProviderFactory
import com.paulrybitskyi.gamedge.data.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.database.GamedgeDatabaseFactory
import com.paulrybitskyi.gamedge.database.tables.GamesTable
import com.paulrybitskyi.gamedge.database.utils.JsonConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object GamesDatabaseDataStoreFactory {


    fun create(context: Context): GamesLocalDataStore {
        return GamesDatabaseDataStore(
            gamesTable = provideGamesTable(context),
            queryTimestampProvider = provideQueryTimestampProvider(),
            entityMapper = provideEntityMapper()
        )
    }


    private fun provideGamesTable(context: Context): GamesTable {
        return GamedgeDatabaseFactory.createGamesTable(context)
    }


    private fun provideQueryTimestampProvider(): QueryTimestampProvider {
        return QueryTimestampProviderFactory.create()
    }


    private fun provideEntityMapper(): EntityMapper {
        return EntityMapper(jsonConverter = provideJsonConverter())
    }


    private fun provideJsonConverter(): JsonConverter {
        return JsonConverter(moshi = provideMoshi())
    }


    private fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }


}