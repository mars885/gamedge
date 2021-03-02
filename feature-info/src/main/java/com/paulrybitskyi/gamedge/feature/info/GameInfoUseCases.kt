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

package com.paulrybitskyi.gamedge.feature.info

import com.paulrybitskyi.gamedge.domain.games.usecases.GetCompanyDevelopedGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.GetGameUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.GetSimilarGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.likes.ObserveGameLikeStateUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.likes.ToggleGameLikeStateUseCase
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject


internal interface GameInfoUseCases {

    val getGameUseCase: GetGameUseCase
    val observeGameLikeStateUseCase: ObserveGameLikeStateUseCase
    val toggleGameLikeStateUseCase: ToggleGameLikeStateUseCase
    val getCompanyDevelopedGamesUseCase: GetCompanyDevelopedGamesUseCase
    val getSimilarGamesUseCase: GetSimilarGamesUseCase

}


@BindType(installIn = BindType.Component.VIEW_MODEL)
internal class GameInfoUseCasesImpl @Inject constructor(
    override val getGameUseCase: GetGameUseCase,
    override val observeGameLikeStateUseCase: ObserveGameLikeStateUseCase,
    override val toggleGameLikeStateUseCase: ToggleGameLikeStateUseCase,
    override val getCompanyDevelopedGamesUseCase: GetCompanyDevelopedGamesUseCase,
    override val getSimilarGamesUseCase: GetSimilarGamesUseCase
) : GameInfoUseCases