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

package com.paulrybitskyi.gamedge.igdb.api.querybuilder

import com.paulrybitskyi.gamedge.commons.data.Constants.POPULAR_GAMES_MIN_POPULARITY
import com.paulrybitskyi.gamedge.commons.data.querying.QueryTimestampProvider
import com.paulrybitskyi.gamedge.igdb.api.entities.Game
import com.paulrybitskyi.gamedge.igdb.api.entities.Game.Schema.HYPE_COUNT
import com.paulrybitskyi.gamedge.igdb.api.entities.Game.Schema.ID
import com.paulrybitskyi.gamedge.igdb.api.entities.Game.Schema.POPULARITY
import com.paulrybitskyi.gamedge.igdb.api.entities.Game.Schema.RELEASE_DATE
import com.paulrybitskyi.gamedge.igdb.apicalypse.querybuilder.ApicalypseQueryBuilderFactory
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.ApicalypseSerializer

internal class IgdbApiQueryBuilderImpl(
    private val apicalypseQueryBuilderFactory: ApicalypseQueryBuilderFactory,
    private val apicalypseSerializer: ApicalypseSerializer,
    private val queryTimestampProvider: QueryTimestampProvider
) : IgdbApiQueryBuilder {


    private val gameEntityFields by lazy {
        apicalypseSerializer.serialize(Game::class.java)
    }


    override fun buildGamesSearchingQuery(searchQuery: String, offset: Int, limit: Int): String {
        return apicalypseQueryBuilderFactory.newBuilder()
            .search(searchQuery)
            .select(gameEntityFields)
            .offset(offset)
            .limit(limit)
            .build()
    }


    override fun buildPopularGamesRetrievalQuery(offset: Int, limit: Int): String {
        val minReleaseDateTimestamp = queryTimestampProvider.getPopularGamesMinReleaseDate()

        return apicalypseQueryBuilderFactory.newBuilder()
            .select(gameEntityFields)
            .where {
                and(
                    { POPULARITY.isNotNull },
                    { POPULARITY.isLargerThan(POPULAR_GAMES_MIN_POPULARITY.toString()) },
                    { RELEASE_DATE.isLargerThan(minReleaseDateTimestamp.toString()) }
                )
            }
            .offset(offset)
            .limit(limit)
            .sortDesc(POPULARITY)
            .build()
    }


    override fun buildRecentlyReleasedGamesRetrievalQuery(offset: Int, limit: Int): String {
        val minReleaseDateTimestamp = queryTimestampProvider.getRecentlyReleasedGamesMinReleaseDate()
        val maxReleaseDateTimestamp = queryTimestampProvider.getRecentlyReleasedGamesMaxReleaseDate()

        return apicalypseQueryBuilderFactory.newBuilder()
            .select(gameEntityFields)
            .where {
                and(
                    { RELEASE_DATE.isLargerThan(minReleaseDateTimestamp.toString()) },
                    { RELEASE_DATE.isSmallerThan(maxReleaseDateTimestamp.toString()) }
                )
            }
            .offset(offset)
            .limit(limit)
            .sortDesc(RELEASE_DATE)
            .build()
    }


    override fun buildComingSoonGamesRetrievalQuery(offset: Int, limit: Int): String {
        val minReleaseDateTimestamp = queryTimestampProvider.getComingSoonGamesMinReleaseDate()

        return apicalypseQueryBuilderFactory.newBuilder()
            .select(gameEntityFields)
            .where { RELEASE_DATE.isLargerThan(minReleaseDateTimestamp.toString()) }
            .offset(offset)
            .limit(limit)
            .sortAsc(RELEASE_DATE)
            .build()
    }


    override fun buildMostAnticipatedGamesRetrievalQuery(offset: Int, limit: Int): String {
        val minReleaseDateTimestamp = queryTimestampProvider.getMostAnticipatedGamesMinReleaseDate()

        return apicalypseQueryBuilderFactory.newBuilder()
            .select(gameEntityFields)
            .where {
                and(
                    { RELEASE_DATE.isLargerThan(minReleaseDateTimestamp.toString()) },
                    { HYPE_COUNT.isNotNull }
                )
            }
            .offset(offset)
            .limit(limit)
            .sortDesc(HYPE_COUNT)
            .build()
    }


    override fun buildGamesRetrievalQuery(gameIds: List<Int>, offset: Int, limit: Int): String {
        val stringifiedGameIds = gameIds.map(Integer::toString)

        return apicalypseQueryBuilderFactory.newBuilder()
            .select(gameEntityFields)
            .where { ID.containsAnyOf(stringifiedGameIds) }
            .offset(offset)
            .limit(limit)
            .build()
    }


}