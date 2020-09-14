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

package com.paulrybitskyi.gamedge.data.usecases.refreshers

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.datastores.GamesDatabaseDataStore
import com.paulrybitskyi.gamedge.data.datastores.GamesServerDataStore
import com.paulrybitskyi.gamedge.data.usecases.mappers.EntityMapper
import com.paulrybitskyi.gamedge.data.usecases.mappers.PaginationMapper
import com.paulrybitskyi.gamedge.data.usecases.mappers.mapToDomainGames
import com.paulrybitskyi.gamedge.data.utils.GamesRefreshingThrottler
import com.paulrybitskyi.gamedge.data.utils.extensions.mapResult
import com.paulrybitskyi.gamedge.data.utils.extensions.onEachSuccess
import com.paulrybitskyi.gamedge.domain.entities.Game
import com.paulrybitskyi.gamedge.domain.usecases.games.commons.GamesRefresherParams
import com.paulrybitskyi.gamedge.domain.usecases.games.refreshers.RefreshMostAnticipatedGamesUseCase
import com.paulrybitskyi.gamedge.domain.utils.DomainResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

internal class RefreshMostAnticipatedGamesUseCaseImpl(
    private val gamesServerDataStore: GamesServerDataStore,
    private val gamesDatabaseDataStore: GamesDatabaseDataStore,
    private val gamesRefreshingThrottler: GamesRefreshingThrottler,
    private val dispatcherProvider: DispatcherProvider,
    private val paginationMapper: PaginationMapper,
    private val entityMapper: EntityMapper
) : RefreshMostAnticipatedGamesUseCase {


    override suspend fun execute(params: GamesRefresherParams): Flow<DomainResult<List<Game>>> {
        return flow {
            if(gamesRefreshingThrottler.canRefreshMostAnticipatedGames()) {
                emit(gamesServerDataStore.getMostAnticipatedGames(paginationMapper.mapToDataPagination(params.pagination)))
            }
        }
        .onEachSuccess {
            gamesDatabaseDataStore.saveGames(it)
            gamesRefreshingThrottler.updateMostAnticipatedGamesLastRefreshingTime()
        }
        .flowOn(dispatcherProvider.main)
        .mapResult(entityMapper::mapToDomainGames, entityMapper::mapToDomainError)
        .flowOn(dispatcherProvider.computation)
    }


}