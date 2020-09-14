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

package com.paulrybitskyi.gamedge.igdb.api.datastore

import com.github.michaelbull.result.mapEither
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.datastores.GamesServerDataStore
import com.paulrybitskyi.gamedge.data.datastores.commons.Pagination
import com.paulrybitskyi.gamedge.data.utils.DataCompany
import com.paulrybitskyi.gamedge.data.utils.DataGame
import com.paulrybitskyi.gamedge.data.utils.DataResult
import com.paulrybitskyi.gamedge.igdb.api.IgdbApi
import com.paulrybitskyi.gamedge.igdb.api.utils.ApiGame
import com.paulrybitskyi.gamedge.igdb.api.utils.ApiResult
import kotlinx.coroutines.withContext

internal class GamesServerDataStoreImpl(
    private val igdbApi: IgdbApi,
    private val entityMapper: EntityMapper,
    private val dispatcherProvider: DispatcherProvider
) : GamesServerDataStore {


    override suspend fun searchGames(searchQuery: String, pagination: Pagination): DataResult<List<DataGame>> {
        return igdbApi
            .searchGames(searchQuery, pagination.offset, pagination.limit)
            .toDataStoreResult()
    }


    override suspend fun getPopularGames(pagination: Pagination): DataResult<List<DataGame>> {
        return igdbApi
            .getPopularGames(pagination.offset, pagination.limit)
            .toDataStoreResult()
    }


    override suspend fun getRecentlyReleasedGames(pagination: Pagination): DataResult<List<DataGame>> {
        return igdbApi
            .getRecentlyReleasedGames(pagination.offset, pagination.limit)
            .toDataStoreResult()
    }


    override suspend fun getComingSoonGames(pagination: Pagination): DataResult<List<DataGame>> {
        return igdbApi
            .getComingSoonGames(pagination.offset, pagination.limit)
            .toDataStoreResult()
    }


    override suspend fun getMostAnticipatedGames(pagination: Pagination): DataResult<List<DataGame>> {
        return igdbApi
            .getMostAnticipatedGames(pagination.offset, pagination.limit)
            .toDataStoreResult()
    }


    override suspend fun getCompanyGames(company: DataCompany, pagination: Pagination): DataResult<List<DataGame>> {
        return igdbApi
            .getGames(company.developedGames, pagination.offset, pagination.limit)
            .toDataStoreResult()
    }


    override suspend fun getSimilarGames(game: DataGame, pagination: Pagination): DataResult<List<DataGame>> {
        return igdbApi
            .getGames(game.similarGames, pagination.offset, pagination.limit)
            .toDataStoreResult()
    }


    private suspend fun ApiResult<List<ApiGame>>.toDataStoreResult(): DataResult<List<DataGame>> {
        return withContext(dispatcherProvider.computation) {
            mapEither(
                success = entityMapper::mapToDataGames,
                failure = entityMapper::mapToDataError
            )
        }
    }


}