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

package com.paulrybitskyi.gamedge.feature.info.presentation

import com.paulrybitskyi.gamedge.feature.info.domain.usecases.GetCompanyDevelopedGamesUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.GetGameImageUrlsUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.GetGameUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.GetSimilarGamesUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.likes.ObserveGameLikeStateUseCase
import com.paulrybitskyi.gamedge.feature.info.domain.usecases.likes.ToggleGameLikeStateUseCase
import javax.inject.Inject

internal class GameInfoUseCases @Inject constructor(
    val getGameUseCase: GetGameUseCase,
    val getGameImageUrlsUseCase: GetGameImageUrlsUseCase,
    val observeGameLikeStateUseCase: ObserveGameLikeStateUseCase,
    val toggleGameLikeStateUseCase: ToggleGameLikeStateUseCase,
    val getCompanyDevelopedGamesUseCase: GetCompanyDevelopedGamesUseCase,
    val getSimilarGamesUseCase: GetSimilarGamesUseCase,
)
