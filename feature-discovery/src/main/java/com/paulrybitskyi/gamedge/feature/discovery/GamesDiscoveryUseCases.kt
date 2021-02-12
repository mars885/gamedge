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

package com.paulrybitskyi.gamedge.feature.discovery

import com.paulrybitskyi.gamedge.domain.games.ObservableGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.discovery.RefreshAllDiscoverableGamesUseCase
import com.paulrybitskyi.gamedge.feature.discovery.di.GamesDiscoveryKey
import javax.inject.Inject

class GamesDiscoveryUseCases @Inject constructor(
    val observeGamesUseCasesMap: Map<GamesDiscoveryKey.Type, @JvmSuppressWildcards ObservableGamesUseCase>,
    val refreshAllDiscoverableGamesUseCase: RefreshAllDiscoverableGamesUseCase
)