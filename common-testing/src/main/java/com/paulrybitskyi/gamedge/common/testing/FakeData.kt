/*
 * Copyright 2022 Paul Rybitskyi, paul.rybitskyi.work@gmail.com
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

@file:Suppress("MagicNumber")

package com.paulrybitskyi.gamedge.common.testing

import com.paulrybitskyi.gamedge.common.data.common.DataPagination
import com.paulrybitskyi.gamedge.common.data.games.DataCategory
import com.paulrybitskyi.gamedge.common.data.games.DataGame
import com.paulrybitskyi.gamedge.common.domain.common.DomainPagination
import com.paulrybitskyi.gamedge.common.domain.common.entities.Error as DomainError
import com.paulrybitskyi.gamedge.common.domain.games.DomainCategory
import com.paulrybitskyi.gamedge.common.domain.games.DomainGame
import com.paulrybitskyi.gamedge.common.api.Error as ApiError
import com.paulrybitskyi.gamedge.common.data.common.entities.Error as DataError

val DOMAIN_GAME = DomainGame(
    id = 1,
    followerCount = null,
    hypeCount = null,
    releaseDate = null,
    criticsRating = null,
    usersRating = null,
    totalRating = null,
    name = "name",
    summary = null,
    storyline = null,
    category = DomainCategory.UNKNOWN,
    cover = null,
    releaseDates = listOf(),
    ageRatings = listOf(),
    videos = listOf(),
    artworks = listOf(),
    screenshots = listOf(),
    genres = listOf(),
    platforms = listOf(),
    playerPerspectives = listOf(),
    themes = listOf(),
    modes = listOf(),
    keywords = listOf(),
    involvedCompanies = listOf(),
    websites = listOf(),
    similarGames = listOf(),
)
val DOMAIN_GAMES = listOf(
    DOMAIN_GAME.copy(id = 1),
    DOMAIN_GAME.copy(id = 2),
    DOMAIN_GAME.copy(id = 3),
)
val DATA_GAME = DataGame(
    id = 1,
    followerCount = null,
    hypeCount = null,
    releaseDate = null,
    criticsRating = null,
    usersRating = null,
    totalRating = null,
    name = "name",
    summary = null,
    storyline = null,
    category = DataCategory.UNKNOWN,
    cover = null,
    releaseDates = listOf(),
    ageRatings = listOf(),
    videos = listOf(),
    artworks = listOf(),
    screenshots = listOf(),
    genres = listOf(),
    platforms = listOf(),
    playerPerspectives = listOf(),
    themes = listOf(),
    modes = listOf(),
    keywords = listOf(),
    involvedCompanies = listOf(),
    websites = listOf(),
    similarGames = listOf(),
)
val DATA_GAMES = listOf(
    DATA_GAME.copy(id = 1),
    DATA_GAME.copy(id = 2),
    DATA_GAME.copy(id = 3),
)

val DOMAIN_PAGINATION = DomainPagination(offset = 0, limit = 20)
val DATA_PAGINATION = DataPagination(offset = 0, limit = 20)

val DOMAIN_ERROR_UNKNOWN = DomainError.Unknown("message")
val DATA_ERROR_UNKNOWN = DataError.Unknown("message")

val API_ERROR_HTTP = ApiError.HttpError(code = 10, message = "message")
val API_ERROR_NETWORK = ApiError.NetworkError(Exception("message"))
val API_ERROR_UNKNOWN = ApiError.UnknownError(Exception("message"))
