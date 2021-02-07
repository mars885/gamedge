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

package com.paulrybitskyi.gamedge.database.games.datastores

import com.paulrybitskyi.gamedge.commons.data.QueryTimestampProvider
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.commons.Pagination
import com.paulrybitskyi.gamedge.data.games.DataCompany
import com.paulrybitskyi.gamedge.data.games.DataGame
import com.paulrybitskyi.gamedge.data.games.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.database.games.DatabaseGame
import com.paulrybitskyi.gamedge.database.games.tables.GamesTable
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
internal class GamesDatabaseDataStoreImpl @Inject constructor(
    private val gamesTable: GamesTable,
    private val dispatcherProvider: DispatcherProvider,
    private val queryTimestampProvider: QueryTimestampProvider,
    private val gameMapper: GameMapper
) : GamesLocalDataStore {


    override suspend fun saveGames(games: List<DataGame>) {
        gamesTable.saveGames(
            withContext(dispatcherProvider.computation) {
                gameMapper.mapToDatabaseGames(games)
            }
        )
    }


    override suspend fun getGame(id: Int): DataGame? {
        return gamesTable.getGame(id)
            ?.let { databaseGame ->
                withContext(dispatcherProvider.computation) {
                    gameMapper.mapToDataGame(databaseGame)
                }
            }
    }


    override suspend fun getCompanyDevelopedGames(company: DataCompany, pagination: Pagination): List<DataGame> {
        return gamesTable.getGames(
            ids = company.developedGames,
            offset = pagination.offset,
            limit = pagination.limit
        )
        .toDataGames()
    }


    override suspend fun getSimilarGames(game: DataGame, pagination: Pagination): List<DataGame> {
        return gamesTable.getGames(
            ids = game.similarGames,
            offset = pagination.offset,
            limit = pagination.limit
        )
        .toDataGames()
    }


    override suspend fun searchGames(searchQuery: String, pagination: Pagination): List<DataGame> {
        return gamesTable.searchGames(
            searchQuery = searchQuery,
            offset = pagination.offset,
            limit = pagination.limit
        )
        .let { databaseGames ->
            withContext(dispatcherProvider.computation) {
                gameMapper.mapToDataGames(databaseGames)
            }
        }
    }


    override suspend fun observePopularGames(pagination: Pagination): Flow<List<DataGame>> {
        return gamesTable.observePopularGames(
            minReleaseDateTimestamp = queryTimestampProvider.getPopularGamesMinReleaseDate(),
            offset = pagination.offset,
            limit = pagination.limit
        )
        .toDataGamesFlow()
    }


    override suspend fun observeRecentlyReleasedGames(pagination: Pagination): Flow<List<DataGame>> {
        return gamesTable.observeRecentlyReleasedGames(
            minReleaseDateTimestamp = queryTimestampProvider.getRecentlyReleasedGamesMinReleaseDate(),
            maxReleaseDateTimestamp = queryTimestampProvider.getRecentlyReleasedGamesMaxReleaseDate(),
            offset = pagination.offset,
            limit = pagination.limit
        )
        .toDataGamesFlow()
    }


    override suspend fun observeComingSoonGames(pagination: Pagination): Flow<List<DataGame>> {
        return gamesTable.observeComingSoonGames(
            minReleaseDateTimestamp = queryTimestampProvider.getComingSoonGamesMinReleaseDate(),
            offset = pagination.offset,
            limit = pagination.limit
        )
        .toDataGamesFlow()
    }


    override suspend fun observeMostAnticipatedGames(pagination: Pagination): Flow<List<DataGame>> {
        return gamesTable.observeMostAnticipatedGames(
            minReleaseDateTimestamp = queryTimestampProvider.getMostAnticipatedGamesMinReleaseDate(),
            offset = pagination.offset,
            limit = pagination.limit
        )
        .toDataGamesFlow()
    }


    private suspend fun List<DatabaseGame>.toDataGames(): List<DataGame> {
        return withContext(dispatcherProvider.computation) {
            gameMapper.mapToDataGames(this@toDataGames)
        }
    }


    private fun Flow<List<DatabaseGame>>.toDataGamesFlow(): Flow<List<DataGame>> {
        return distinctUntilChanged()
            .map(gameMapper::mapToDataGames)
            .flowOn(dispatcherProvider.computation)
    }


}