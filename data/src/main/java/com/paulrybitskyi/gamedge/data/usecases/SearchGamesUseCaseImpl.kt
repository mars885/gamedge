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
import com.paulrybitskyi.gamedge.core.extensions.asSuccess
import com.paulrybitskyi.gamedge.core.extensions.onSuccess
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.providers.NetworkStateProvider
import com.paulrybitskyi.gamedge.data.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.data.datastores.GamesRemoteDataStore
import com.paulrybitskyi.gamedge.data.usecases.mapper.EntityMapper
import com.paulrybitskyi.gamedge.data.usecases.mapper.mapToDomainGames
import com.paulrybitskyi.gamedge.domain.usecases.SearchGamesUseCase
import com.paulrybitskyi.gamedge.domain.usecases.SearchGamesUseCase.Params
import kotlinx.coroutines.withContext

internal class SearchGamesUseCaseImpl(
    private val gamesLocalDataStore: GamesLocalDataStore,
    private val gamesRemoteDataStore: GamesRemoteDataStore,
    private val dispatcherProvider: DispatcherProvider,
    private val networkStateProvider: NetworkStateProvider,
    private val entityMapper: EntityMapper
) : SearchGamesUseCase {


    override suspend fun execute(params: Params) = withContext(dispatcherProvider.io) {
        val searchQuery = params.searchQuery
        val offset = params.pagination.offset
        val limit = params.pagination.limit

        if(networkStateProvider.isNetworkAvailable) {
            gamesRemoteDataStore
                .searchGames(searchQuery, offset, limit)
                .onSuccess(gamesLocalDataStore::saveGames)
                .mapEither(entityMapper::mapToDomainGames, entityMapper::mapToDomainError)
        } else {
            gamesLocalDataStore
                .searchGames(searchQuery, offset, limit)
                .let(entityMapper::mapToDomainGames)
                .asSuccess()
        }
    }


}