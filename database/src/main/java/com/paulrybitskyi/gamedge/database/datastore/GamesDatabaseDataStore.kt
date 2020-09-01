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

import com.paulrybitskyi.gamedge.commons.data.extensions.asSuccess
import com.paulrybitskyi.gamedge.commons.data.querying.QueryTimestampProvider
import com.paulrybitskyi.gamedge.data.datastores.GamesDataStore
import com.paulrybitskyi.gamedge.data.utils.DataCompany
import com.paulrybitskyi.gamedge.data.utils.DataGame
import com.paulrybitskyi.gamedge.data.utils.DataStoreResult
import com.paulrybitskyi.gamedge.database.tables.GamesTable
import com.paulrybitskyi.gamedge.database.utils.DatabaseGame

internal class GamesDatabaseDataStore(
    private val gamesTable: GamesTable,
    private val queryTimestampProvider: QueryTimestampProvider,
    private val entityMapper: EntityMapper
) : GamesDataStore {


    override suspend fun searchGames(searchQuery: String, offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return gamesTable.searchGames(
            searchQuery = searchQuery,
            offset = offset,
            limit = limit
        )
        .toDataStoreResult()
    }


    override suspend fun getPopularGames(offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return gamesTable.getPopularGames(
            minReleaseDateTimestamp = queryTimestampProvider.getPopularGamesMinReleaseDate(),
            offset = offset,
            limit = limit
        )
        .toDataStoreResult()
    }


    override suspend fun getRecentlyReleasedGames(offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return gamesTable.getRecentlyReleasedGames(
            minReleaseDateTimestamp = queryTimestampProvider.getRecentlyReleasedGamesMinReleaseDate(),
            maxReleaseDateTimestamp = queryTimestampProvider.getRecentlyReleasedGamesMaxReleaseDate(),
            offset = offset,
            limit = limit
        )
        .toDataStoreResult()
    }


    override suspend fun getComingSoonGames(offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return gamesTable.getComingSoonGames(
            minReleaseDateTimestamp = queryTimestampProvider.getComingSoonGamesMinReleaseDate(),
            offset = offset,
            limit = limit
        )
        .toDataStoreResult()
    }


    override suspend fun getMostAnticipatedGames(offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return gamesTable.getMostAnticipatedGames(
            minReleaseDateTimestamp = queryTimestampProvider.getMostAnticipatedGamesMinReleaseDate(),
            offset = offset,
            limit = limit
        )
        .toDataStoreResult()
    }


    override suspend fun getCompanyGames(company: DataCompany, offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return gamesTable.getGames(
            ids = company.developedGames,
            offset = offset,
            limit = limit
        )
        .toDataStoreResult()
    }


    override suspend fun getSimilarGames(game: DataGame, offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return gamesTable.getGames(
            ids = game.similarGames,
            offset = offset,
            limit = limit
        )
        .toDataStoreResult()
    }


    private fun List<DatabaseGame>.toDataStoreResult(): DataStoreResult<List<DataGame>> {
        return entityMapper.mapToDataGames(this).asSuccess()
    }


}