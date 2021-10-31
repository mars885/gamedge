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

import com.paulrybitskyi.gamedge.commons.data.QueryTimestampProvider
import com.paulrybitskyi.gamedge.igdb.api.games.entities.Game
import com.paulrybitskyi.gamedge.igdb.api.games.entities.Game.Schema.HYPE_COUNT
import com.paulrybitskyi.gamedge.igdb.api.games.entities.Game.Schema.ID
import com.paulrybitskyi.gamedge.igdb.api.games.entities.Game.Schema.RELEASE_DATE
import com.paulrybitskyi.gamedge.igdb.api.games.entities.Game.Schema.TOTAL_RATING
import com.paulrybitskyi.gamedge.igdb.api.games.entities.Game.Schema.USERS_RATING
import com.paulrybitskyi.gamedge.igdb.apicalypse.querybuilder.ApicalypseQueryBuilderFactory
import com.paulrybitskyi.gamedge.igdb.apicalypse.serialization.ApicalypseSerializer
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject


internal interface IgdbApiQueryFactory {

    fun createGamesSearchingQuery(searchQuery: String, offset: Int, limit: Int): String

    fun createPopularGamesRetrievalQuery(offset: Int, limit: Int): String

    fun createRecentlyReleasedGamesRetrievalQuery(offset: Int, limit: Int): String

    fun createComingSoonGamesRetrievalQuery(offset: Int, limit: Int): String

    fun createMostAnticipatedGamesRetrievalQuery(offset: Int, limit: Int): String

    fun createGamesRetrievalQuery(gameIds: List<Int>, offset: Int, limit: Int): String

}


@BindType
internal class IgdbApiQueryFactoryImpl @Inject constructor(
    private val apicalypseQueryBuilderFactory: ApicalypseQueryBuilderFactory,
    private val apicalypseSerializer: ApicalypseSerializer,
    private val queryTimestampProvider: QueryTimestampProvider
) : IgdbApiQueryFactory {


    private val gameEntityFields by lazy {
        apicalypseSerializer.serialize(Game::class.java)
    }


    override fun createGamesSearchingQuery(searchQuery: String, offset: Int, limit: Int): String {
        return apicalypseQueryBuilderFactory.create()
            .search(searchQuery)
            .select(gameEntityFields)
            .offset(offset)
            .limit(limit)
            .build()
    }


    override fun createPopularGamesRetrievalQuery(offset: Int, limit: Int): String {
        val minReleaseDateTimestamp = queryTimestampProvider.getPopularGamesMinReleaseDate()

        return apicalypseQueryBuilderFactory.create()
            .select(gameEntityFields)
            .where {
                USERS_RATING.isNotNull and
                { RELEASE_DATE.isLargerThan(minReleaseDateTimestamp.toString()) }
            }
            .offset(offset)
            .limit(limit)
            .sortDesc(TOTAL_RATING)
            .build()
    }


    override fun createRecentlyReleasedGamesRetrievalQuery(offset: Int, limit: Int): String {
        val minReleaseDateTimestamp = queryTimestampProvider.getRecentlyReleasedGamesMinReleaseDate()
        val maxReleaseDateTimestamp = queryTimestampProvider.getRecentlyReleasedGamesMaxReleaseDate()

        return apicalypseQueryBuilderFactory.create()
            .select(gameEntityFields)
            .where {
                RELEASE_DATE.isLargerThan(minReleaseDateTimestamp.toString()) and
                { RELEASE_DATE.isSmallerThan(maxReleaseDateTimestamp.toString()) }
            }
            .offset(offset)
            .limit(limit)
            .sortDesc(RELEASE_DATE)
            .build()
    }


    override fun createComingSoonGamesRetrievalQuery(offset: Int, limit: Int): String {
        val minReleaseDateTimestamp = queryTimestampProvider.getComingSoonGamesMinReleaseDate()

        return apicalypseQueryBuilderFactory.create()
            .select(gameEntityFields)
            .where { RELEASE_DATE.isLargerThan(minReleaseDateTimestamp.toString()) }
            .offset(offset)
            .limit(limit)
            .sortAsc(RELEASE_DATE)
            .build()
    }


    override fun createMostAnticipatedGamesRetrievalQuery(offset: Int, limit: Int): String {
        val minReleaseDateTimestamp = queryTimestampProvider.getMostAnticipatedGamesMinReleaseDate()

        return apicalypseQueryBuilderFactory.create()
            .select(gameEntityFields)
            .where {
                RELEASE_DATE.isLargerThan(minReleaseDateTimestamp.toString()) and
                { HYPE_COUNT.isNotNull }
            }
            .offset(offset)
            .limit(limit)
            .sortDesc(HYPE_COUNT)
            .build()
    }


    override fun createGamesRetrievalQuery(gameIds: List<Int>, offset: Int, limit: Int): String {
        val stringifiedGameIds = gameIds.map(Integer::toString)

        return apicalypseQueryBuilderFactory.create()
            .select(gameEntityFields)
            .where { ID.containsAnyOf(stringifiedGameIds) }
            .offset(offset)
            .limit(limit)
            .build()
    }


}
