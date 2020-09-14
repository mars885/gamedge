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

package com.paulrybitskyi.gamedge.data.usecases

import com.github.michaelbull.result.mapEither
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.providers.NetworkStateProvider
import com.paulrybitskyi.gamedge.core.utils.asSuccess
import com.paulrybitskyi.gamedge.core.utils.onSuccess
import com.paulrybitskyi.gamedge.data.datastores.GamesDatabaseDataStore
import com.paulrybitskyi.gamedge.data.datastores.GamesServerDataStore
import com.paulrybitskyi.gamedge.data.usecases.mappers.EntityMapper
import com.paulrybitskyi.gamedge.data.usecases.mappers.PaginationMapper
import com.paulrybitskyi.gamedge.data.usecases.mappers.mapToDomainGames
import com.paulrybitskyi.gamedge.domain.usecases.games.SearchGamesUseCase
import com.paulrybitskyi.gamedge.domain.usecases.games.SearchGamesUseCase.Params
import kotlinx.coroutines.withContext

internal class SearchGamesUseCaseImpl(
    private val gamesDatabaseDataStore: GamesDatabaseDataStore,
    private val gamesServerDataStore: GamesServerDataStore,
    private val dispatcherProvider: DispatcherProvider,
    private val networkStateProvider: NetworkStateProvider,
    private val paginationMapper: PaginationMapper,
    private val entityMapper: EntityMapper
) : SearchGamesUseCase {


    override suspend fun execute(params: Params) = withContext(dispatcherProvider.io) {
        val searchQuery = params.searchQuery
        val pagination = paginationMapper.mapToDataPagination(params.pagination)

        if(networkStateProvider.isNetworkAvailable) {
            gamesServerDataStore
                .searchGames(searchQuery, pagination)
                .onSuccess(gamesDatabaseDataStore::saveGames)
                .mapEither(entityMapper::mapToDomainGames, entityMapper::mapToDomainError)
        } else {
            gamesDatabaseDataStore
                .searchGames(searchQuery, pagination)
                .let(entityMapper::mapToDomainGames)
                .asSuccess()
        }
    }


}