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

package com.paulrybitskyi.gamedge.data.games.usecases.refreshers

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.utils.mapResult
import com.paulrybitskyi.gamedge.data.commons.utils.onEachSuccess
import com.paulrybitskyi.gamedge.data.games.GamesRefreshingThrottler
import com.paulrybitskyi.gamedge.data.games.datastores.commons.GamesDataStores
import com.paulrybitskyi.gamedge.data.games.usecases.commons.mapToDomainGames
import com.paulrybitskyi.gamedge.data.games.usecases.refreshers.commons.RefreshGamesUseCaseMappers
import com.paulrybitskyi.gamedge.domain.commons.DomainResult
import com.paulrybitskyi.gamedge.domain.games.commons.RefreshGamesUseCaseParams
import com.paulrybitskyi.gamedge.domain.games.entities.Game
import com.paulrybitskyi.gamedge.domain.games.usecases.refreshers.RefreshPopularGamesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

internal class RefreshPopularGamesUseCaseImpl(
    private val gamesDataStores: GamesDataStores,
    private val gamesRefreshingThrottler: GamesRefreshingThrottler,
    private val dispatcherProvider: DispatcherProvider,
    private val mappers: RefreshGamesUseCaseMappers
) : RefreshPopularGamesUseCase {


    override suspend fun execute(params: RefreshGamesUseCaseParams): Flow<DomainResult<List<Game>>> {
        return flow {
            if(gamesRefreshingThrottler.canRefreshPopularGames()) {
                emit(gamesDataStores.remote.getPopularGames(mappers.pagination.mapToDataPagination(params.pagination)))
            }
        }
        .onEachSuccess {
            gamesDataStores.local.saveGames(it)
            gamesRefreshingThrottler.updatePopularGamesLastRefreshingTime()
        }
        .flowOn(dispatcherProvider.main)
        .mapResult(mappers.game::mapToDomainGames, mappers.error::mapToDomainError)
        .flowOn(dispatcherProvider.computation)
    }


}