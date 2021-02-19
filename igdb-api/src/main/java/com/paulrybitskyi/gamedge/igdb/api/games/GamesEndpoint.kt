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

package com.paulrybitskyi.gamedge.igdb.api.games

import com.paulrybitskyi.gamedge.commons.api.ApiResult
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject
import javax.inject.Singleton


internal interface GamesEndpoint {

    suspend fun searchGames(searchQuery: String, offset: Int, limit: Int): ApiResult<List<ApiGame>>

    suspend fun getPopularGames(offset: Int, limit: Int): ApiResult<List<ApiGame>>

    suspend fun getRecentlyReleasedGames(offset: Int, limit: Int): ApiResult<List<ApiGame>>

    suspend fun getComingSoonGames(offset: Int, limit: Int): ApiResult<List<ApiGame>>

    suspend fun getMostAnticipatedGames(offset: Int, limit: Int): ApiResult<List<ApiGame>>

    suspend fun getGames(gameIds: List<Int>, offset: Int, limit: Int): ApiResult<List<ApiGame>>

}


@Singleton
@BindType
internal class GamesEndpointImpl @Inject constructor(
    private val gamesService: GamesService,
    private val igdbApiQueryFactory: IgdbApiQueryFactory
) : GamesEndpoint {


    override suspend fun searchGames(
        searchQuery: String,
        offset: Int,
        limit: Int
    ): ApiResult<List<ApiGame>> {
        return gamesService.getGames(
            igdbApiQueryFactory.createGamesSearchingQuery(
                searchQuery = searchQuery,
                offset = offset,
                limit = limit
            )
        )
    }


    override suspend fun getPopularGames(offset: Int, limit: Int): ApiResult<List<ApiGame>> {
        return gamesService.getGames(
            igdbApiQueryFactory.createPopularGamesRetrievalQuery(
                offset = offset,
                limit = limit
            )
        )
    }


    override suspend fun getRecentlyReleasedGames(offset: Int, limit: Int): ApiResult<List<ApiGame>> {
        return gamesService.getGames(
            igdbApiQueryFactory.createRecentlyReleasedGamesRetrievalQuery(
                offset = offset,
                limit = limit
            )
        )
    }


    override suspend fun getComingSoonGames(offset: Int, limit: Int): ApiResult<List<ApiGame>> {
        return gamesService.getGames(
            igdbApiQueryFactory.createComingSoonGamesRetrievalQuery(
                offset = offset,
                limit = limit
            )
        )
    }


    override suspend fun getMostAnticipatedGames(offset: Int, limit: Int): ApiResult<List<ApiGame>> {
        return gamesService.getGames(
            igdbApiQueryFactory.createMostAnticipatedGamesRetrievalQuery(
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
        return gamesService.getGames(
            igdbApiQueryFactory.createGamesRetrievalQuery(
                gameIds = gameIds,
                offset = offset,
                limit = limit
            )
        )
    }


}