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

import com.paulrybitskyi.gamedge.commons.data.querying.QueryTimestampProvider
import com.paulrybitskyi.gamedge.data.datastores.GamesDatabaseDataStore
import com.paulrybitskyi.gamedge.data.utils.DataCompany
import com.paulrybitskyi.gamedge.data.utils.DataGame
import com.paulrybitskyi.gamedge.database.tables.GamesTable
import com.paulrybitskyi.gamedge.database.utils.DatabaseGame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class GamesDatabaseDataStoreImpl(
    private val gamesTable: GamesTable,
    private val queryTimestampProvider: QueryTimestampProvider,
    private val entityMapper: EntityMapper
) : GamesDatabaseDataStore {


    override suspend fun saveGames(games: List<DataGame>) {
        gamesTable.saveGames(entityMapper.mapToDatabaseGames(games))
    }


    override suspend fun searchGames(searchQuery: String, offset: Int, limit: Int): List<DataGame> {
        return gamesTable.searchGames(
            searchQuery = searchQuery,
            offset = offset,
            limit = limit
        )
        .let(entityMapper::mapToDataGames)
    }


    override suspend fun observePopularGames(offset: Int, limit: Int): Flow<List<DataGame>> {
        return gamesTable.observePopularGames(
            minReleaseDateTimestamp = queryTimestampProvider.getPopularGamesMinReleaseDate(),
            offset = offset,
            limit = limit
        )
        .toDataGamesFlow()
    }


    override suspend fun observeRecentlyReleasedGames(offset: Int, limit: Int): Flow<List<DataGame>> {
        return gamesTable.observeRecentlyReleasedGames(
            minReleaseDateTimestamp = queryTimestampProvider.getRecentlyReleasedGamesMinReleaseDate(),
            maxReleaseDateTimestamp = queryTimestampProvider.getRecentlyReleasedGamesMaxReleaseDate(),
            offset = offset,
            limit = limit
        )
        .toDataGamesFlow()
    }


    override suspend fun observeComingSoonGames(offset: Int, limit: Int): Flow<List<DataGame>> {
        return gamesTable.observeComingSoonGames(
            minReleaseDateTimestamp = queryTimestampProvider.getComingSoonGamesMinReleaseDate(),
            offset = offset,
            limit = limit
        )
        .toDataGamesFlow()
    }


    override suspend fun observeMostAnticipatedGames(offset: Int, limit: Int): Flow<List<DataGame>> {
        return gamesTable.observeMostAnticipatedGames(
            minReleaseDateTimestamp = queryTimestampProvider.getMostAnticipatedGamesMinReleaseDate(),
            offset = offset,
            limit = limit
        )
        .toDataGamesFlow()
    }


    override suspend fun observeCompanyGames(company: DataCompany, offset: Int, limit: Int): Flow<List<DataGame>> {
        return gamesTable.observeGames(
            ids = company.developedGames,
            offset = offset,
            limit = limit
        )
        .toDataGamesFlow()
    }


    override suspend fun observeSimilarGames(game: DataGame, offset: Int, limit: Int): Flow<List<DataGame>> {
        return gamesTable.observeGames(
            ids = game.similarGames,
            offset = offset,
            limit = limit
        )
        .toDataGamesFlow()
    }


    private fun Flow<List<DatabaseGame>>.toDataGamesFlow(): Flow<List<DataGame>> {
        return distinctUntilChanged().map(entityMapper::mapToDataGames)
    }


}