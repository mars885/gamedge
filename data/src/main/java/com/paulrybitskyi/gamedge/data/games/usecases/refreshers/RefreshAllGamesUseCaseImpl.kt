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
import com.paulrybitskyi.gamedge.core.utils.mapError
import com.paulrybitskyi.gamedge.core.utils.resultOrError
import com.paulrybitskyi.gamedge.data.commons.DataResult
import com.paulrybitskyi.gamedge.data.commons.utils.onEachSuccess
import com.paulrybitskyi.gamedge.data.games.DataGame
import com.paulrybitskyi.gamedge.data.games.GamesRefreshingThrottler
import com.paulrybitskyi.gamedge.data.games.datastores.GamesRemoteDataStore
import com.paulrybitskyi.gamedge.data.games.datastores.commons.GamesDataStores
import com.paulrybitskyi.gamedge.data.games.datastores.commons.Pagination
import com.paulrybitskyi.gamedge.data.games.usecases.commons.mapToDomainGames
import com.paulrybitskyi.gamedge.data.games.usecases.refreshers.commons.RefreshGamesUseCaseMappers
import com.paulrybitskyi.gamedge.domain.games.DomainGame
import com.paulrybitskyi.gamedge.domain.games.commons.RefreshGamesUseCaseParams
import com.paulrybitskyi.gamedge.domain.games.usecases.refreshers.RefreshAllGamesUseCase
import kotlinx.coroutines.flow.*

internal class RefreshAllGamesUseCaseImpl(
    private val gamesDataStores: GamesDataStores,
    private val gamesRefreshingThrottler: GamesRefreshingThrottler,
    private val dispatcherProvider: DispatcherProvider,
    private val mappers: RefreshGamesUseCaseMappers
) : RefreshAllGamesUseCase {


    override suspend fun execute(params: RefreshGamesUseCaseParams): Flow<List<DomainGame>> {
        val flows = listOf(
            refreshPopularGames(params),
            refreshRecentlyReleasedGames(params),
            refreshComingSoonGames(params),
            refreshMostAnticipatedGames(params)
        )

        return combine(flows) {
            it.toList().flatten()
        }
        .flowOn(dispatcherProvider.computation)
        .onEach(gamesDataStores.local::saveGames)
        .flowOn(dispatcherProvider.main)
        .map(mappers.game::mapToDomainGames)
        .flowOn(dispatcherProvider.computation)
    }


    private suspend fun refreshPopularGames(
        params: RefreshGamesUseCaseParams
    ): Flow<List<DataGame>> {
        return refreshGames(
            params = params,
            canRefreshGames = { canRefreshPopularGames() },
            getGames = { getPopularGames(it) },
            updateLastRefreshingTime = { updatePopularGamesLastRefreshingTime() }
        )
    }


    private suspend fun refreshRecentlyReleasedGames(
        params: RefreshGamesUseCaseParams
    ): Flow<List<DataGame>> {
        return refreshGames(
            params = params,
            canRefreshGames = { canRefreshRecentlyReleasedGames() },
            getGames = { getRecentlyReleasedGames(it) },
            updateLastRefreshingTime = { updateRecentlyReleasedGamesLastRefreshingTime() }
        )
    }


    private suspend fun refreshComingSoonGames(
        params: RefreshGamesUseCaseParams
    ): Flow<List<DataGame>> {
        return refreshGames(
            params = params,
            canRefreshGames = { canRefreshComingSoonGames() },
            getGames = { getComingSoonGames(it) },
            updateLastRefreshingTime = { updateComingSoonGamesLastRefreshingTime() }
        )
    }


    private suspend fun refreshMostAnticipatedGames(
        params: RefreshGamesUseCaseParams
    ): Flow<List<DataGame>> {
        return refreshGames(
            params = params,
            canRefreshGames = { canRefreshMostAnticipatedGames() },
            getGames = { getMostAnticipatedGames(it) },
            updateLastRefreshingTime = { updateMostAnticipatedGamesLastRefreshingTime() }
        )
    }


    private suspend fun refreshGames(
        params: RefreshGamesUseCaseParams,
        canRefreshGames: suspend GamesRefreshingThrottler.() -> Boolean,
        getGames: suspend GamesRemoteDataStore.(Pagination) -> DataResult<List<DataGame>>,
        updateLastRefreshingTime: suspend GamesRefreshingThrottler.() -> Unit
    ): Flow<List<DataGame>> {
        return flow {
            if(gamesRefreshingThrottler.canRefreshGames()) {
                emit(gamesDataStores.remote.getGames(mappers.pagination.mapToDataPagination(params.pagination)))
            }
        }
        .onEachSuccess { gamesRefreshingThrottler.updateLastRefreshingTime() }
        .flowOn(dispatcherProvider.main)
        .mapError(mappers.error::mapToDomainError)
        .resultOrError()
    }


}