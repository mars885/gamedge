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

import com.paulrybitskyi.gamedge.common.api.ApiResult
import com.paulrybitskyi.gamedge.igdb.api.games.entities.ApiGame
import com.paulrybitskyi.gamedge.igdb.api.games.requests.GetComingSoonGamesRequest
import com.paulrybitskyi.gamedge.igdb.api.games.requests.GetGamesRequest
import com.paulrybitskyi.gamedge.igdb.api.games.requests.GetMostAnticipatedGamesRequest
import com.paulrybitskyi.gamedge.igdb.api.games.requests.GetPopularGamesRequest
import com.paulrybitskyi.gamedge.igdb.api.games.requests.GetRecentlyReleasedGamesRequest
import com.paulrybitskyi.gamedge.igdb.api.games.requests.SearchGamesRequest
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject
import javax.inject.Singleton

interface GamesEndpoint {
    suspend fun searchGames(request: SearchGamesRequest): ApiResult<List<ApiGame>>
    suspend fun getPopularGames(request: GetPopularGamesRequest): ApiResult<List<ApiGame>>
    suspend fun getRecentlyReleasedGames(request: GetRecentlyReleasedGamesRequest): ApiResult<List<ApiGame>>
    suspend fun getComingSoonGames(request: GetComingSoonGamesRequest): ApiResult<List<ApiGame>>
    suspend fun getMostAnticipatedGames(request: GetMostAnticipatedGamesRequest): ApiResult<List<ApiGame>>
    suspend fun getGames(request: GetGamesRequest): ApiResult<List<ApiGame>>
}

@Singleton
@BindType
internal class GamesEndpointImpl @Inject constructor(
    private val gamesService: GamesService,
    private val igdbApiQueryFactory: IgdbApiQueryFactory,
) : GamesEndpoint {

    override suspend fun searchGames(request: SearchGamesRequest): ApiResult<List<ApiGame>> {
        return gamesService.getGames(
            igdbApiQueryFactory.createGamesSearchingQuery(request),
        )
    }

    override suspend fun getPopularGames(request: GetPopularGamesRequest): ApiResult<List<ApiGame>> {
        return gamesService.getGames(
            igdbApiQueryFactory.createPopularGamesRetrievalQuery(request),
        )
    }

    override suspend fun getRecentlyReleasedGames(
        request: GetRecentlyReleasedGamesRequest
    ): ApiResult<List<ApiGame>> {
        return gamesService.getGames(
            igdbApiQueryFactory.createRecentlyReleasedGamesRetrievalQuery(request),
        )
    }

    override suspend fun getComingSoonGames(request: GetComingSoonGamesRequest): ApiResult<List<ApiGame>> {
        return gamesService.getGames(
            igdbApiQueryFactory.createComingSoonGamesRetrievalQuery(request),
        )
    }

    override suspend fun getMostAnticipatedGames(
        request: GetMostAnticipatedGamesRequest
    ): ApiResult<List<ApiGame>> {
        return gamesService.getGames(
            igdbApiQueryFactory.createMostAnticipatedGamesRetrievalQuery(request),
        )
    }

    override suspend fun getGames(request: GetGamesRequest): ApiResult<List<ApiGame>> {
        return gamesService.getGames(
            igdbApiQueryFactory.createGamesRetrievalQuery(request),
        )
    }
}
