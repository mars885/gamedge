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

package com.paulrybitskyi.gamedge.feature.info

import com.paulrybitskyi.gamedge.common.testing.DOMAIN_GAME
import com.paulrybitskyi.gamedge.common.testing.DOMAIN_PAGINATION
import com.paulrybitskyi.gamedge.common.domain.games.DomainCompany
import com.paulrybitskyi.gamedge.feature.info.domain.GetCompanyDevelopedGamesUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.GetGameUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.GetSimilarGamesUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.RefreshCompanyDevelopedGamesUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.RefreshSimilarGamesUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.likes.ObserveGameLikeStateUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.likes.ToggleGameLikeStateUseCase

private val DOMAIN_COMPANY = DomainCompany(
    id = 1,
    name = "name",
    websiteUrl = "url",
    logo = null,
    developedGames = listOf(1, 2, 3),
)

internal val OBSERVE_GAME_LIKE_STATE_USE_CASE_PARAMS = ObserveGameLikeStateUseCase.Params(gameId = 10)
internal val TOGGLE_GAME_LIKE_STATE_USE_CASE_PARAMS = ToggleGameLikeStateUseCase.Params(gameId = 10)
internal val GET_GAME_USE_CASE_PARAMS = GetGameUseCase.Params(gameId = 10)
internal val GET_COMPANY_DEVELOPED_GAMES_USE_CASE_PARAMS = GetCompanyDevelopedGamesUseCase.Params(
    DOMAIN_COMPANY,
    DOMAIN_PAGINATION,
)
internal val REFRESH_COMPANY_DEVELOPED_GAMES_USE_CASE_PARAMS = RefreshCompanyDevelopedGamesUseCase.Params(
    DOMAIN_COMPANY,
    DOMAIN_PAGINATION,
)
internal val GET_SIMILAR_GAMES_USE_CASE_PARAMS = GetSimilarGamesUseCase.Params(DOMAIN_GAME, DOMAIN_PAGINATION)
internal val REFRESH_SIMILAR_GAMES_USE_CASE_PARAMS = RefreshSimilarGamesUseCase.Params(DOMAIN_GAME, DOMAIN_PAGINATION)
