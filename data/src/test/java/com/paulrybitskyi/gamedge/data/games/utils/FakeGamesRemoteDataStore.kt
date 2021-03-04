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

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.paulrybitskyi.gamedge.data.commons.DataPagination
import com.paulrybitskyi.gamedge.data.commons.DataResult
import com.paulrybitskyi.gamedge.data.commons.entities.Error
import com.paulrybitskyi.gamedge.data.games.DataCompany
import com.paulrybitskyi.gamedge.data.games.DataGame
import com.paulrybitskyi.gamedge.data.games.datastores.GamesRemoteDataStore

internal class FakeGamesRemoteDataStore : GamesRemoteDataStore {


    var shouldReturnGames = false
    var shouldReturnError = false


    override suspend fun searchGames(
        searchQuery: String,
        pagination: DataPagination
    ): DataResult<List<DataGame>> {
        return getGames()
    }


    override suspend fun getPopularGames(pagination: DataPagination): DataResult<List<DataGame>> {
        return getGames()
    }


    override suspend fun getRecentlyReleasedGames(pagination: DataPagination): DataResult<List<DataGame>> {
        return getGames()
    }


    override suspend fun getComingSoonGames(pagination: DataPagination): DataResult<List<DataGame>> {
        return getGames()
    }


    override suspend fun getMostAnticipatedGames(pagination: DataPagination): DataResult<List<DataGame>> {
        return getGames()
    }


    override suspend fun getCompanyDevelopedGames(
        company: DataCompany,
        pagination: DataPagination
    ): DataResult<List<DataGame>> {
        return getGames()
    }


    override suspend fun getSimilarGames(
        game: DataGame,
        pagination: DataPagination
    ): DataResult<List<DataGame>> {
        return getGames()
    }


    private fun getGames(): DataResult<List<DataGame>> {
        return when {
            shouldReturnGames -> Ok(DATA_GAMES)
            shouldReturnError -> Err(Error.Unknown("error"))

            else -> throw IllegalStateException()
        }
    }


}