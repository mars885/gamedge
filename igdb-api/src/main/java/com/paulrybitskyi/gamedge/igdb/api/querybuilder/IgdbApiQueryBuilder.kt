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

import com.paulrybitskyi.gamedge.igdb.api.entities.Company
import com.paulrybitskyi.gamedge.igdb.api.entities.Game

internal interface IgdbApiQueryBuilder {

    fun buildGamesSearchingQuery(searchQuery: String, offset: Int, limit: Int): String

    fun buildPopularGamesRetrievalQuery(offset: Int, limit: Int): String

    fun buildRecentlyReleasedGamesRetrievalQuery(offset: Int, limit: Int): String

    fun buildComingSoonGamesRetrievalQuery(offset: Int, limit: Int): String

    fun buildMostAnticipatedGamesRetrievalQuery(offset: Int, limit: Int): String

    fun buildCompanyGamesRetrievalQuery(company: Company, offset: Int, limit: Int): String

    fun buildSimilarGamesRetrievalQuery(game: Game, offset: Int, limit: Int): String

}