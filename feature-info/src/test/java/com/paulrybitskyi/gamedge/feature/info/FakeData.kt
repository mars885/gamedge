/*
 * Copyright 2022 Paul Rybitskyi, oss@paulrybitskyi.com
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

package com.paulrybitskyi.gamedge.feature.info

import com.paulrybitskyi.gamedge.common.domain.games.entities.Company
import com.paulrybitskyi.gamedge.common.domain.games.entities.InvolvedCompany
import com.paulrybitskyi.gamedge.common.testing.domain.DOMAIN_GAME
import com.paulrybitskyi.gamedge.common.testing.domain.DOMAIN_GAMES
import com.paulrybitskyi.gamedge.common.testing.domain.PAGINATION
import com.paulrybitskyi.gamedge.feature.info.domain.entities.GameInfo
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.GetCompanyDevelopedGamesUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.GetGameUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.GetSimilarGamesUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.RefreshCompanyDevelopedGamesUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.RefreshSimilarGamesUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.likes.ObserveGameLikeStateUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.likes.ToggleGameLikeStateUseCase

private val COMPANY = Company(
    id = 1,
    name = "name",
    websiteUrl = "url",
    logo = null,
    developedGames = listOf(1, 2, 3),
)

val GAME_INFO = GameInfo(
    game = DOMAIN_GAME,
    isGameLiked = true,
    companyGames = DOMAIN_GAMES,
    similarGames = DOMAIN_GAMES,
)
val INVOLVED_COMPANY = InvolvedCompany(
    company = COMPANY,
    isDeveloper = false,
    isPublisher = false,
    isPorter = false,
    isSupporting = false,
)

internal val OBSERVE_GAME_LIKE_STATE_USE_CASE_PARAMS = ObserveGameLikeStateUseCase.Params(gameId = 10)
internal val TOGGLE_GAME_LIKE_STATE_USE_CASE_PARAMS = ToggleGameLikeStateUseCase.Params(gameId = 10)
internal val GET_GAME_USE_CASE_PARAMS = GetGameUseCase.Params(gameId = 10)
internal val GET_COMPANY_DEVELOPED_GAMES_USE_CASE_PARAMS = GetCompanyDevelopedGamesUseCase.Params(
    COMPANY,
    PAGINATION,
)
internal val REFRESH_COMPANY_DEVELOPED_GAMES_USE_CASE_PARAMS = RefreshCompanyDevelopedGamesUseCase.Params(
    COMPANY,
    PAGINATION,
)
internal val GET_SIMILAR_GAMES_USE_CASE_PARAMS = GetSimilarGamesUseCase.Params(DOMAIN_GAME, PAGINATION)
internal val REFRESH_SIMILAR_GAMES_USE_CASE_PARAMS = RefreshSimilarGamesUseCase.Params(DOMAIN_GAME, PAGINATION)
