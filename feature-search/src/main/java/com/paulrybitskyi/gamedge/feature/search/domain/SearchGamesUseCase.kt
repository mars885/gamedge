/*
 * Copyright 2020 Paul Rybitskyi, oss@paulrybitskyi.com
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

package com.paulrybitskyi.gamedge.feature.search.domain

import com.paulrybitskyi.gamedge.common.domain.common.DispatcherProvider
import com.paulrybitskyi.gamedge.common.domain.common.DomainResult
import com.paulrybitskyi.gamedge.common.domain.common.entities.Pagination
import com.paulrybitskyi.gamedge.common.domain.common.extensions.asSuccess
import com.paulrybitskyi.gamedge.common.domain.common.extensions.onSuccess
import com.paulrybitskyi.gamedge.common.domain.common.usecases.UseCase
import com.paulrybitskyi.gamedge.common.domain.games.datastores.GamesDataStores
import com.paulrybitskyi.gamedge.common.domain.games.entities.Game
import com.paulrybitskyi.gamedge.core.providers.NetworkStateProvider
import com.paulrybitskyi.gamedge.feature.search.domain.SearchGamesUseCase.Params
import com.paulrybitskyi.hiltbinder.BindType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

internal interface SearchGamesUseCase : UseCase<Params, Flow<DomainResult<List<Game>>>> {

    data class Params(
        val searchQuery: String,
        val pagination: Pagination = Pagination(),
    )
}

@Singleton
@BindType
internal class SearchGamesUseCaseImpl @Inject constructor(
    private val gamesDataStores: GamesDataStores,
    private val dispatcherProvider: DispatcherProvider,
    private val networkStateProvider: NetworkStateProvider,
) : SearchGamesUseCase {

    private companion object {
        private const val LOCAL_SEARCH_DELAY_TIMEOUT = 150L
    }

    override suspend fun execute(params: Params): Flow<DomainResult<List<Game>>> {
        return flow { emit(searchGames(params)) }
            .flowOn(dispatcherProvider.main)
    }

    private suspend fun searchGames(params: Params): DomainResult<List<Game>> {
        val searchQuery = params.searchQuery
        val pagination = params.pagination

        if (networkStateProvider.isNetworkAvailable) {
            return gamesDataStores.remote
                .searchGames(searchQuery, pagination)
                .onSuccess(gamesDataStores.local::saveGames)
        }

        return gamesDataStores.local
            .searchGames(searchQuery, pagination)
            .asSuccess()
            // Delaying to give a sense of "loading" since it's really fast without it
            .also { delay(LOCAL_SEARCH_DELAY_TIMEOUT) }
    }
}
