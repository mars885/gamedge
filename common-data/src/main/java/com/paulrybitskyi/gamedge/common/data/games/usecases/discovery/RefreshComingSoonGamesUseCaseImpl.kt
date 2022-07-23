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

package com.paulrybitskyi.gamedge.common.data.games.usecases.discovery

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.utils.mapResult
import com.paulrybitskyi.gamedge.common.data.common.utils.onEachSuccess
import com.paulrybitskyi.gamedge.common.data.common.utils.toDataPagination
import com.paulrybitskyi.gamedge.common.data.games.datastores.GamesDataStores
import com.paulrybitskyi.gamedge.common.data.games.usecases.common.RefreshGamesUseCaseMappers
import com.paulrybitskyi.gamedge.common.data.games.usecases.common.mapToDomainGames
import com.paulrybitskyi.gamedge.common.data.games.usecases.common.throttling.GamesRefreshingThrottlerTools
import com.paulrybitskyi.gamedge.common.domain.common.DomainResult
import com.paulrybitskyi.gamedge.common.domain.games.common.RefreshGamesUseCaseParams
import com.paulrybitskyi.gamedge.common.domain.games.entities.Game
import com.paulrybitskyi.gamedge.common.domain.games.usecases.RefreshComingSoonGamesUseCase
import com.paulrybitskyi.hiltbinder.BindType
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@Singleton
@BindType
internal class RefreshComingSoonGamesUseCaseImpl @Inject constructor(
    private val gamesDataStores: GamesDataStores,
    private val dispatcherProvider: DispatcherProvider,
    private val throttlerTools: GamesRefreshingThrottlerTools,
    private val mappers: RefreshGamesUseCaseMappers,
) : RefreshComingSoonGamesUseCase {

    override fun execute(params: RefreshGamesUseCaseParams): Flow<DomainResult<List<Game>>> {
        val throttlerKey = throttlerTools.keyProvider.provideComingSoonGamesKey(params.pagination)

        return flow {
            if (throttlerTools.throttler.canRefreshGames(throttlerKey)) {
                emit(gamesDataStores.remote.getComingSoonGames(params.pagination.toDataPagination()))
            }
        }
        .onEachSuccess { games ->
            gamesDataStores.local.saveGames(games)
            throttlerTools.throttler.updateGamesLastRefreshTime(throttlerKey)
        }
        .flowOn(dispatcherProvider.main)
        .mapResult(mappers.game::mapToDomainGames, mappers.error::mapToDomainError)
        .flowOn(dispatcherProvider.computation)
    }
}
