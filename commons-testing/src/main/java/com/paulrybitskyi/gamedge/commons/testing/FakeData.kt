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

package com.paulrybitskyi.gamedge.commons.testing

import com.paulrybitskyi.gamedge.commons.api.Error as ApiError
import com.paulrybitskyi.gamedge.data.articles.DataArticle
import com.paulrybitskyi.gamedge.data.auth.DataOauthCredentials
import com.paulrybitskyi.gamedge.data.commons.DataPagination
import com.paulrybitskyi.gamedge.data.commons.entities.Error as DataError
import com.paulrybitskyi.gamedge.data.games.DataCategory
import com.paulrybitskyi.gamedge.data.games.DataCompany
import com.paulrybitskyi.gamedge.data.games.DataGame
import com.paulrybitskyi.gamedge.domain.articles.DomainArticle
import com.paulrybitskyi.gamedge.domain.articles.usecases.ObserveArticlesUseCase
import com.paulrybitskyi.gamedge.domain.articles.usecases.RefreshArticlesUseCase
import com.paulrybitskyi.gamedge.domain.auth.DomainOauthCredentials
import com.paulrybitskyi.gamedge.domain.commons.DomainPagination
import com.paulrybitskyi.gamedge.domain.games.*
import com.paulrybitskyi.gamedge.domain.commons.entities.Error as DomainError
import com.paulrybitskyi.gamedge.domain.games.commons.ObserveGamesUseCaseParams
import com.paulrybitskyi.gamedge.domain.games.commons.RefreshGamesUseCaseParams
import com.paulrybitskyi.gamedge.domain.games.usecases.*
import com.paulrybitskyi.gamedge.domain.games.usecases.likes.ObserveGameLikeStateUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.likes.ToggleGameLikeStateUseCase


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
    similarGames = listOf()
)
val DOMAIN_GAMES = listOf(
    DOMAIN_GAME.copy(id = 1),
    DOMAIN_GAME.copy(id = 2),
    DOMAIN_GAME.copy(id = 3)
)
val DOMAIN_IMAGE = DomainImage(
    id = "id",
    width = 500,
    height = 1000
)
val DOMAIN_VIDEO = DomainVideo(
    id = "id",
    name = "name"
)
val DOMAIN_COMPANY = DomainCompany(
    id = 1,
    name = "name",
    websiteUrl = "url",
    logo = null,
    developedGames = listOf(1, 2, 3)
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
    similarGames = listOf()
)
val DATA_GAMES = listOf(
    DATA_GAME.copy(id = 1),
    DATA_GAME.copy(id = 2),
    DATA_GAME.copy(id = 3)
)
val DATA_COMPANY = DataCompany(
    id = 1,
    name = "name",
    websiteUrl = "website_url",
    logo = null,
    developedGames = listOf(1, 2, 3)
)


val DOMAIN_ARTICLE = DomainArticle(
    id = 1,
    title = "title",
    lede = "lede",
    imageUrls = emptyMap(),
    publicationDate = 500L,
    siteDetailUrl = "site_detail_url"
)
val DOMAIN_ARTICLES = listOf(
    DOMAIN_ARTICLE.copy(id = 1),
    DOMAIN_ARTICLE.copy(id = 2),
    DOMAIN_ARTICLE.copy(id = 3),
)
val DATA_ARTICLE = DataArticle(
    id = 1,
    title = "title",
    lede = "lede",
    imageUrls = emptyMap(),
    publicationDate = 500L,
    siteDetailUrl = "site_detail_url"
)
val DATA_ARTICLES = listOf(
    DATA_ARTICLE.copy(id = 1),
    DATA_ARTICLE.copy(id = 2),
    DATA_ARTICLE.copy(id = 3),
)


val DOMAIN_OAUTH_CREDENTIALS = DomainOauthCredentials(
    accessToken = "access_token",
    tokenType = "token_type",
    tokenTtl = 5000L
)
val DATA_OAUTH_CREDENTIALS = DataOauthCredentials(
    accessToken = "access_token",
    tokenType = "token_type",
    tokenTtl = 5000L
)


val DOMAIN_PAGINATION = DomainPagination(offset = 0, limit = 20)
val DATA_PAGINATION = DataPagination(offset = 0, limit = 20)


val DOMAIN_ERROR_API = DomainError.ApiError.ClientError("message")
val DOMAIN_ERROR_NOT_FOUND = DomainError.NotFound("message")
val DOMAIN_ERROR_UNKNOWN = DomainError.Unknown("message")
val DATA_ERROR_NOT_FOUND = DataError.NotFound("message")
val DATA_ERROR_UNKNOWN = DataError.Unknown("message")
val API_ERROR_HTTP = ApiError.HttpError(code = 10, message = "message")
val API_ERROR_NETWORK = ApiError.NetworkError(Exception("message"))
val API_ERROR_UNKNOWN = ApiError.UnknownError(Exception("message"))


val OBSERVE_ARTICLES_USE_CASE_PARAMS = ObserveArticlesUseCase.Params(true, DOMAIN_PAGINATION)
val REFRESH_ARTICLES_USE_CASE_PARAMS = RefreshArticlesUseCase.Params()
val OBSERVE_GAMES_USE_CASE_PARAMS = ObserveGamesUseCaseParams()
val REFRESH_GAMES_USE_CASE_PARAMS = RefreshGamesUseCaseParams()
val OBSERVE_GAME_LIKE_STATE_USE_CASE_PARAMS = ObserveGameLikeStateUseCase.Params(gameId = 10)
val TOGGLE_GAME_LIKE_STATE_USE_CASE_PARAMS = ToggleGameLikeStateUseCase.Params(gameId = 10)
val GET_GAME_USE_CASE_PARAMS = GetGameUseCase.Params(gameId = 10)
val GET_COMPANY_DEVELOPED_GAMES_USE_CASE_PARAMS = GetCompanyDevelopedGamesUseCase.Params(DOMAIN_COMPANY, DOMAIN_PAGINATION)
val REFRESH_COMPANY_DEVELOPED_GAMES_USE_CASE_PARAMS = RefreshCompanyDevelopedGamesUseCase.Params(DOMAIN_COMPANY, DOMAIN_PAGINATION)
val GET_SIMILAR_GAMES_USE_CASE_PARAMS = GetSimilarGamesUseCase.Params(DOMAIN_GAME, DOMAIN_PAGINATION)
val REFRESH_SIMILAR_GAMES_USE_CASE_PARAMS = RefreshSimilarGamesUseCase.Params(DOMAIN_GAME, DOMAIN_PAGINATION)