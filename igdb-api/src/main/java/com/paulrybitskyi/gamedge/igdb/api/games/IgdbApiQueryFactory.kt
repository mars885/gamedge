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

package com.paulrybitskyi.gamedge.igdb.api.games

import com.paulrybitskyi.gamedge.igdb.api.games.entities.ApiGame
import com.paulrybitskyi.gamedge.igdb.api.games.entities.ApiGame.Schema.HYPE_COUNT
import com.paulrybitskyi.gamedge.igdb.api.games.entities.ApiGame.Schema.ID
import com.paulrybitskyi.gamedge.igdb.api.games.entities.ApiGame.Schema.RELEASE_DATE
import com.paulrybitskyi.gamedge.igdb.api.games.entities.ApiGame.Schema.TOTAL_RATING
import com.paulrybitskyi.gamedge.igdb.api.games.entities.ApiGame.Schema.USERS_RATING
import com.paulrybitskyi.gamedge.igdb.api.games.requests.GetComingSoonGamesRequest
import com.paulrybitskyi.gamedge.igdb.api.games.requests.GetGamesRequest
import com.paulrybitskyi.gamedge.igdb.api.games.requests.GetMostAnticipatedGamesRequest
import com.paulrybitskyi.gamedge.igdb.api.games.requests.GetPopularGamesRequest
import com.paulrybitskyi.gamedge.igdb.api.games.requests.GetRecentlyReleasedGamesRequest
import com.paulrybitskyi.gamedge.igdb.api.games.requests.SearchGamesRequest
import com.paulrybitskyi.gamedge.igdb.apicalypse.querybuilder.ApicalypseQueryBuilderFactory
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.ApicalypseSerializer
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject

internal interface IgdbApiQueryFactory {
    fun createGamesSearchingQuery(request: SearchGamesRequest): String
    fun createPopularGamesRetrievalQuery(request: GetPopularGamesRequest): String
    fun createRecentlyReleasedGamesRetrievalQuery(request: GetRecentlyReleasedGamesRequest): String
    fun createComingSoonGamesRetrievalQuery(request: GetComingSoonGamesRequest): String
    fun createMostAnticipatedGamesRetrievalQuery(request: GetMostAnticipatedGamesRequest): String
    fun createGamesRetrievalQuery(request: GetGamesRequest): String
}

@BindType
internal class IgdbApiQueryFactoryImpl @Inject constructor(
    private val apicalypseQueryBuilderFactory: ApicalypseQueryBuilderFactory,
    private val apicalypseSerializer: ApicalypseSerializer,
) : IgdbApiQueryFactory {

    private val gameEntityFields by lazy {
        apicalypseSerializer.serialize(ApiGame::class.java)
    }

    override fun createGamesSearchingQuery(request: SearchGamesRequest): String {
        return apicalypseQueryBuilderFactory.create()
            .search(request.searchQuery)
            .select(gameEntityFields)
            .offset(request.offset)
            .limit(request.limit)
            .build()
    }

    override fun createPopularGamesRetrievalQuery(request: GetPopularGamesRequest): String {
        return apicalypseQueryBuilderFactory.create()
            .select(gameEntityFields)
            .where {
                USERS_RATING.isNotNull and
                { RELEASE_DATE.isLargerThan(request.minReleaseDateTimestamp.toString()) }
            }
            .offset(request.offset)
            .limit(request.limit)
            .sortDesc(TOTAL_RATING)
            .build()
    }

    override fun createRecentlyReleasedGamesRetrievalQuery(request: GetRecentlyReleasedGamesRequest): String {
        return apicalypseQueryBuilderFactory.create()
            .select(gameEntityFields)
            .where {
                RELEASE_DATE.isLargerThan(request.minReleaseDateTimestamp.toString()) and
                { RELEASE_DATE.isSmallerThan(request.maxReleaseDateTimestamp.toString()) }
            }
            .offset(request.offset)
            .limit(request.limit)
            .sortDesc(RELEASE_DATE)
            .build()
    }

    override fun createComingSoonGamesRetrievalQuery(request: GetComingSoonGamesRequest): String {
        return apicalypseQueryBuilderFactory.create()
            .select(gameEntityFields)
            .where { RELEASE_DATE.isLargerThan(request.minReleaseDateTimestamp.toString()) }
            .offset(request.offset)
            .limit(request.limit)
            .sortAsc(RELEASE_DATE)
            .build()
    }

    override fun createMostAnticipatedGamesRetrievalQuery(request: GetMostAnticipatedGamesRequest): String {
        return apicalypseQueryBuilderFactory.create()
            .select(gameEntityFields)
            .where {
                RELEASE_DATE.isLargerThan(request.minReleaseDateTimestamp.toString()) and
                { HYPE_COUNT.isNotNull }
            }
            .offset(request.offset)
            .limit(request.limit)
            .sortDesc(HYPE_COUNT)
            .build()
    }

    override fun createGamesRetrievalQuery(request: GetGamesRequest): String {
        val stringifiedGameIds = request.gameIds.map(Integer::toString)

        return apicalypseQueryBuilderFactory.create()
            .select(gameEntityFields)
            .where { ID.containsAnyOf(stringifiedGameIds) }
            .offset(request.offset)
            .limit(request.limit)
            .build()
    }
}
