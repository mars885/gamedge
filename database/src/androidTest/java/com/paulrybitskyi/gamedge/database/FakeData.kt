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

package com.paulrybitskyi.gamedge.database

import com.paulrybitskyi.gamedge.database.articles.DatabaseArticle
import com.paulrybitskyi.gamedge.database.games.entities.DbCategory
import com.paulrybitskyi.gamedge.database.games.entities.DbGame
import com.paulrybitskyi.gamedge.database.games.entities.DbLikedGame

internal val DB_ARTICLE = DatabaseArticle(
    id = 1,
    title = "title",
    lede = "lede",
    imageUrls = mapOf(),
    publicationDate = 0L,
    siteDetailUrl = "site_detail_url"
)
internal val DB_ARTICLES = listOf(
    DB_ARTICLE.copy(id = 1, publicationDate = 10L, title = "Article1"),
    DB_ARTICLE.copy(id = 2, publicationDate = 20L, title = "Article2"),
    DB_ARTICLE.copy(id = 3, publicationDate = 30L, title = "Article3"),
    DB_ARTICLE.copy(id = 4, publicationDate = 40L, title = "Article4"),
    DB_ARTICLE.copy(id = 5, publicationDate = 50L, title = "Article5"),
)

internal val DB_GAME = DbGame(
    id = 1,
    followerCount = null,
    hypeCount = null,
    releaseDate = null,
    criticsRating = null,
    usersRating = null,
    totalRating = 50.0,
    name = "Game1",
    summary = null,
    storyline = null,
    category = DbCategory.UNKNOWN,
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
    similarGames = listOf()
)
internal val DB_GAMES = listOf(
    DB_GAME.copy(
        id = 1,
        name = "Game1",
        hypeCount = 1,
        releaseDate = 100L,
        usersRating = 5.0,
        totalRating = 10.0
    ),
    DB_GAME.copy(
        id = 2,
        name = "Game2",
        hypeCount = 2,
        releaseDate = 200L,
        usersRating = 10.0,
        totalRating = 20.0
    ),
    DB_GAME.copy(
        id = 3,
        name = "Game3",
        hypeCount = 3,
        releaseDate = 300L,
        usersRating = 15.0,
        totalRating = 30.0
    ),
    DB_GAME.copy(
        id = 4,
        name = "Game4",
        hypeCount = 4,
        releaseDate = 400L,
        usersRating = 20.0,
        totalRating = 40.0
    ),
    DB_GAME.copy(
        id = 5,
        name = "Game5",
        hypeCount = 5,
        releaseDate = 500L,
        usersRating = 25.0,
        totalRating = 50.0
    )
)

internal val DB_LIKED_GAME = DbLikedGame(id = 1, gameId = DB_GAME.id, likeTimestamp = 0L)
internal val DB_LIKED_GAMES = listOf(
    DbLikedGame(id = 1, gameId = DB_GAMES[0].id, likeTimestamp = 1000L),
    DbLikedGame(id = 2, gameId = DB_GAMES[1].id, likeTimestamp = 2000L),
    DbLikedGame(id = 3, gameId = DB_GAMES[2].id, likeTimestamp = 3000L),
    DbLikedGame(id = 4, gameId = DB_GAMES[3].id, likeTimestamp = 4000L),
    DbLikedGame(id = 5, gameId = DB_GAMES[4].id, likeTimestamp = 5000L)
)
