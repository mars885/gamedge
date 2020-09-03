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

package com.paulrybitskyi.gamedge.igdb.api

import com.paulrybitskyi.gamedge.igdb.api.querybuilder.IgdbApiQueryBuilder
import com.paulrybitskyi.gamedge.igdb.api.services.IgdbApiService
import com.paulrybitskyi.gamedge.igdb.api.utils.ApiGame
import com.paulrybitskyi.gamedge.igdb.api.utils.ApiResult

internal class IgdbApiImpl(
    private val igdbApiService: IgdbApiService,
    private val igdbApiQueryBuilder: IgdbApiQueryBuilder
) : IgdbApi {


    override suspend fun searchGames(
        searchQuery: String,
        offset: Int,
        limit: Int
    ): ApiResult<List<ApiGame>> {
        return igdbApiService.getGames(
            igdbApiQueryBuilder.buildGamesSearchingQuery(
                searchQuery = searchQuery,
                offset = offset,
                limit = limit
            )
        )
    }


    override suspend fun getPopularGames(offset: Int, limit: Int): ApiResult<List<ApiGame>> {
        return igdbApiService.getGames(
            igdbApiQueryBuilder.buildPopularGamesRetrievalQuery(
                offset = offset,
                limit = limit
            )
        )
    }


    override suspend fun getRecentlyReleasedGames(offset: Int, limit: Int): ApiResult<List<ApiGame>> {
        return igdbApiService.getGames(
            igdbApiQueryBuilder.buildRecentlyReleasedGamesRetrievalQuery(
                offset = offset,
                limit = limit
            )
        )
    }


    override suspend fun getComingSoonGames(offset: Int, limit: Int): ApiResult<List<ApiGame>> {
        return igdbApiService.getGames(
            igdbApiQueryBuilder.buildComingSoonGamesRetrievalQuery(
                offset = offset,
                limit = limit
            )
        )
    }


    override suspend fun getMostAnticipatedGames(offset: Int, limit: Int): ApiResult<List<ApiGame>> {
        return igdbApiService.getGames(
            igdbApiQueryBuilder.buildMostAnticipatedGamesRetrievalQuery(
                offset = offset,
                limit = limit
            )
        )
    }


    override suspend fun getGames(
        gameIds: List<Int>,
        offset: Int,
        limit: Int
    ): ApiResult<List<ApiGame>> {
        return igdbApiService.getGames(
            igdbApiQueryBuilder.buildGamesRetrievalQuery(
                gameIds = gameIds,
                offset = offset,
                limit = limit
            )
        )
    }


}