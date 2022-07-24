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

package com.paulrybitskyi.gamedge.common.domain.games.usecases

import com.paulrybitskyi.gamedge.common.domain.common.DispatcherProvider
import com.paulrybitskyi.gamedge.common.domain.common.DomainResult
import com.paulrybitskyi.gamedge.common.domain.common.extensions.onEachSuccess
import com.paulrybitskyi.gamedge.common.domain.games.RefreshableGamesUseCase
import com.paulrybitskyi.gamedge.common.domain.games.common.RefreshGamesUseCaseParams
import com.paulrybitskyi.gamedge.common.domain.games.common.throttling.GamesRefreshingThrottlerTools
import com.paulrybitskyi.gamedge.common.domain.games.datastores.GamesDataStores
import com.paulrybitskyi.gamedge.common.domain.games.entities.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface RefreshComingSoonGamesUseCase : RefreshableGamesUseCase

@Singleton
internal class RefreshComingSoonGamesUseCaseImpl @Inject constructor(
    private val gamesDataStores: GamesDataStores,
    private val throttlerTools: GamesRefreshingThrottlerTools,
    private val dispatcherProvider: DispatcherProvider,
) : RefreshComingSoonGamesUseCase {

    override fun execute(params: RefreshGamesUseCaseParams): Flow<DomainResult<List<Game>>> {
        val throttlerKey = throttlerTools.keyProvider.provideComingSoonGamesKey(params.pagination)

        return flow {
            if (throttlerTools.throttler.canRefreshGames(throttlerKey)) {
                emit(gamesDataStores.remote.getComingSoonGames(params.pagination))
            }
        }
        .onEachSuccess { games ->
            gamesDataStores.local.saveGames(games)
            throttlerTools.throttler.updateGamesLastRefreshTime(throttlerKey)
        }
        .flowOn(dispatcherProvider.main)
    }
}
