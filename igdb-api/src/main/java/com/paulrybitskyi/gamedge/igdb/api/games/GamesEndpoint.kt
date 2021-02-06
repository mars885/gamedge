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
import com.paulrybitskyi.gamedge.igdb.api.auth.Authorizer
import com.paulrybitskyi.gamedge.igdb.api.auth.entities.AuthorizationType
import com.paulrybitskyi.gamedge.igdb.api.games.querybuilder.IgdbApiQueryBuilder


internal interface GamesEndpoint {

    suspend fun searchGames(searchQuery: String, offset: Int, limit: Int): ApiResult<List<ApiGame>>

    suspend fun getPopularGames(offset: Int, limit: Int): ApiResult<List<ApiGame>>

    suspend fun getRecentlyReleasedGames(offset: Int, limit: Int): ApiResult<List<ApiGame>>

    suspend fun getComingSoonGames(offset: Int, limit: Int): ApiResult<List<ApiGame>>

    suspend fun getMostAnticipatedGames(offset: Int, limit: Int): ApiResult<List<ApiGame>>

    suspend fun getGames(gameIds: List<Int>, offset: Int, limit: Int): ApiResult<List<ApiGame>>

}


internal class GamesEndpointImpl(
    private val gamesService: GamesService,
    private val igdbApiQueryBuilder: IgdbApiQueryBuilder,
    private val authorizer: Authorizer,
    private val clientId: String
) : GamesEndpoint {


    override suspend fun searchGames(
        searchQuery: String,
        offset: Int,
        limit: Int
    ): ApiResult<List<ApiGame>> {
        return getGames(
            igdbApiQueryBuilder.buildGamesSearchingQuery(
                searchQuery = searchQuery,
                offset = offset,
                limit = limit
            )
        )
    }


    override suspend fun getPopularGames(offset: Int, limit: Int): ApiResult<List<ApiGame>> {
        return getGames(
            igdbApiQueryBuilder.buildPopularGamesRetrievalQuery(
                offset = offset,
                limit = limit
            )
        )
    }


    override suspend fun getRecentlyReleasedGames(offset: Int, limit: Int): ApiResult<List<ApiGame>> {
        return getGames(
            igdbApiQueryBuilder.buildRecentlyReleasedGamesRetrievalQuery(
                offset = offset,
                limit = limit
            )
        )
    }


    override suspend fun getComingSoonGames(offset: Int, limit: Int): ApiResult<List<ApiGame>> {
        return getGames(
            igdbApiQueryBuilder.buildComingSoonGamesRetrievalQuery(
                offset = offset,
                limit = limit
            )
        )
    }


    override suspend fun getMostAnticipatedGames(offset: Int, limit: Int): ApiResult<List<ApiGame>> {
        return getGames(
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
        return getGames(
            igdbApiQueryBuilder.buildGamesRetrievalQuery(
                gameIds = gameIds,
                offset = offset,
                limit = limit
            )
        )
    }


    private suspend fun getGames(body: String): ApiResult<List<ApiGame>> {
        val authorizationHeader = authorizer.buildAuthorizationHeader(AuthorizationType.BEARER)

        return gamesService.getGames(
            clientId = clientId,
            authorization = authorizationHeader,
            body = body
        )
    }


}