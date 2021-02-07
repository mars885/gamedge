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

package com.paulrybitskyi.gamedge.data.games.usecases.commons.throttling

import com.paulrybitskyi.gamedge.domain.commons.entities.Pagination
import com.paulrybitskyi.gamedge.domain.games.entities.Company
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject


internal interface GamesRefreshingThrottlerKeyBuilder {

    fun buildPopularGamesKey(pagination: Pagination): String

    fun buildRecentlyReleasedGamesKey(pagination: Pagination): String

    fun buildComingSoonGamesKey(pagination: Pagination): String

    fun buildMostAnticipatedGamesKey(pagination: Pagination): String

    fun buildCompanyDevelopedGamesKey(company: Company, pagination: Pagination): String

    fun buildSimilarGamesKey(game: Game, pagination: Pagination): String

}


@BindType
internal class GamesRefreshingThrottlerKeyBuilderImpl @Inject constructor() : GamesRefreshingThrottlerKeyBuilder {


    override fun buildPopularGamesKey(pagination: Pagination): String {
        return "popular_games | offset: ${pagination.offset} | limit: ${pagination.limit}"
    }


    override fun buildRecentlyReleasedGamesKey(pagination: Pagination): String {
        return "recently_released_games | offset: ${pagination.offset} | limit: ${pagination.limit}"
    }


    override fun buildComingSoonGamesKey(pagination: Pagination): String {
        return "coming_soon_games | offset: ${pagination.offset} | limit: ${pagination.limit}"
    }


    override fun buildMostAnticipatedGamesKey(pagination: Pagination): String {
        return "most_anticipated_games | offset: ${pagination.offset} | limit: ${pagination.limit}"
    }


    override fun buildCompanyDevelopedGamesKey(company: Company, pagination: Pagination): String {
        return """
            company_developed_games | 
            company_id: ${company.id} | 
            developed_games_ids: ${company.developedGames.sorted()} | 
            offset: ${pagination.offset} | 
            limit: ${pagination.limit}
        """.trimIndent()
    }


    override fun buildSimilarGamesKey(game: Game, pagination: Pagination): String {
        return """
            similar_games | 
            game_id: ${game.id} | 
            similar_games_ids: ${game.similarGames.sorted()} | 
            offset: ${pagination.offset} | 
            limit: ${pagination.limit}
        """.trimIndent()
    }


}