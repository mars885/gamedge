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

package com.paulrybitskyi.gamedge.data.games.utils

import com.paulrybitskyi.gamedge.data.commons.DataPagination
import com.paulrybitskyi.gamedge.data.games.DataCompany
import com.paulrybitskyi.gamedge.data.games.DataGame
import com.paulrybitskyi.gamedge.data.games.datastores.GamesLocalDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class FakeGamesLocalDataStore : GamesLocalDataStore {


    var games = listOf<DataGame>()


    override suspend fun saveGames(games: List<DataGame>) {
        this.games = games
    }


    override suspend fun getGame(id: Int): DataGame? {
        return null // no-up
    }


    override suspend fun getCompanyDevelopedGames(company: DataCompany, pagination: DataPagination): List<DataGame> {
        return emptyList() // no-up
    }


    override suspend fun getSimilarGames(game: DataGame, pagination: DataPagination): List<DataGame> {
        return emptyList() // no-up
    }


    override suspend fun searchGames(
        searchQuery: String,
        pagination: DataPagination
    ): List<DataGame> {
        return emptyList() // no-up
    }


    override suspend fun observePopularGames(pagination: DataPagination): Flow<List<DataGame>> {
        return flowOf(emptyList()) // no-up
    }


    override suspend fun observeRecentlyReleasedGames(pagination: DataPagination): Flow<List<DataGame>> {
        return flowOf(emptyList()) // no-up
    }


    override suspend fun observeComingSoonGames(pagination: DataPagination): Flow<List<DataGame>> {
        return flowOf(emptyList()) // no-up
    }


    override suspend fun observeMostAnticipatedGames(pagination: DataPagination): Flow<List<DataGame>> {
        return flowOf(emptyList()) // no-up
    }


}