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

package com.paulrybitskyi.gamedge.igdb.api.games.datastores

import com.github.michaelbull.result.mapEither
import com.paulrybitskyi.gamedge.commons.api.ApiResult
import com.paulrybitskyi.gamedge.commons.api.ErrorMapper
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.commons.DataResult
import com.paulrybitskyi.gamedge.data.commons.Pagination
import com.paulrybitskyi.gamedge.data.games.DataCompany
import com.paulrybitskyi.gamedge.data.games.DataGame
import com.paulrybitskyi.gamedge.data.games.datastores.GamesRemoteDataStore
import com.paulrybitskyi.gamedge.igdb.api.games.ApiGame
import com.paulrybitskyi.gamedge.igdb.api.games.GamesEndpoint
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.withContext

@Singleton
@BindType
internal class GamesIgdbDataStore @Inject constructor(
    private val gamesEndpoint: GamesEndpoint,
    private val dispatcherProvider: DispatcherProvider,
    private val gameMapper: GameMapper,
    private val errorMapper: ErrorMapper
) : GamesRemoteDataStore {

    override suspend fun searchGames(searchQuery: String, pagination: Pagination): DataResult<List<DataGame>> {
        return gamesEndpoint
            .searchGames(searchQuery, pagination.offset, pagination.limit)
            .toDataStoreResult()
    }

    override suspend fun getPopularGames(pagination: Pagination): DataResult<List<DataGame>> {
        return gamesEndpoint
            .getPopularGames(pagination.offset, pagination.limit)
            .toDataStoreResult()
    }

    override suspend fun getRecentlyReleasedGames(pagination: Pagination): DataResult<List<DataGame>> {
        return gamesEndpoint
            .getRecentlyReleasedGames(pagination.offset, pagination.limit)
            .toDataStoreResult()
    }

    override suspend fun getComingSoonGames(pagination: Pagination): DataResult<List<DataGame>> {
        return gamesEndpoint
            .getComingSoonGames(pagination.offset, pagination.limit)
            .toDataStoreResult()
    }

    override suspend fun getMostAnticipatedGames(pagination: Pagination): DataResult<List<DataGame>> {
        return gamesEndpoint
            .getMostAnticipatedGames(pagination.offset, pagination.limit)
            .toDataStoreResult()
    }

    override suspend fun getCompanyDevelopedGames(
        company: DataCompany,
        pagination: Pagination
    ): DataResult<List<DataGame>> {
        return gamesEndpoint
            .getGames(company.developedGames, pagination.offset, pagination.limit)
            .toDataStoreResult()
    }

    override suspend fun getSimilarGames(game: DataGame, pagination: Pagination): DataResult<List<DataGame>> {
        return gamesEndpoint
            .getGames(game.similarGames, pagination.offset, pagination.limit)
            .toDataStoreResult()
    }

    private suspend fun ApiResult<List<ApiGame>>.toDataStoreResult(): DataResult<List<DataGame>> {
        return withContext(dispatcherProvider.computation) {
            mapEither(gameMapper::mapToDataGames, errorMapper::mapToDataError)
        }
    }
}
