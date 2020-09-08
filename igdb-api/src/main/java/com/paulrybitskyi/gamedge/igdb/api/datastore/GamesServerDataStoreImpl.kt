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
import com.paulrybitskyi.gamedge.data.datastores.GamesServerDataStore
import com.paulrybitskyi.gamedge.data.utils.DataCompany
import com.paulrybitskyi.gamedge.data.utils.DataGame
import com.paulrybitskyi.gamedge.data.utils.DataStoreResult
import com.paulrybitskyi.gamedge.igdb.api.IgdbApi
import com.paulrybitskyi.gamedge.igdb.api.utils.ApiGame
import com.paulrybitskyi.gamedge.igdb.api.utils.ApiResult

internal class GamesServerDataStoreImpl(
    private val igdbApi: IgdbApi,
    private val entityMapper: EntityMapper
) : GamesServerDataStore {


    override suspend fun searchGames(searchQuery: String, offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return igdbApi
            .searchGames(searchQuery, offset, limit)
            .toDataStoreResult()
    }


    override suspend fun getPopularGames(offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return igdbApi
            .getPopularGames(offset, limit)
            .toDataStoreResult()
    }


    override suspend fun getRecentlyReleasedGames(offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return igdbApi
            .getRecentlyReleasedGames(offset, limit)
            .toDataStoreResult()
    }


    override suspend fun getComingSoonGames(offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return igdbApi
            .getComingSoonGames(offset, limit)
            .toDataStoreResult()
    }


    override suspend fun getMostAnticipatedGames(offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return igdbApi
            .getMostAnticipatedGames(offset, limit)
            .toDataStoreResult()
    }


    override suspend fun getCompanyGames(company: DataCompany, offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return igdbApi
            .getGames(company.developedGames, offset, limit)
            .toDataStoreResult()
    }


    override suspend fun getSimilarGames(game: DataGame, offset: Int, limit: Int): DataStoreResult<List<DataGame>> {
        return igdbApi
            .getGames(game.similarGames, offset, limit)
            .toDataStoreResult()
    }


    private fun ApiResult<List<ApiGame>>.toDataStoreResult(): DataStoreResult<List<DataGame>> {
        return mapEither(
            success = entityMapper::mapToDataGames,
            failure = entityMapper::mapToDataError
        )
    }


}