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

package com.paulrybitskyi.gamedge.data.games.usecases

import com.github.michaelbull.result.mapEither
import com.paulrybitskyi.hiltbinder.BindType
import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.core.providers.NetworkStateProvider
import com.paulrybitskyi.gamedge.core.utils.asSuccess
import com.paulrybitskyi.gamedge.core.utils.onSuccess
import com.paulrybitskyi.gamedge.data.commons.ErrorMapper
import com.paulrybitskyi.gamedge.data.games.datastores.commons.GamesDataStores
import com.paulrybitskyi.gamedge.data.games.usecases.commons.GameMapper
import com.paulrybitskyi.gamedge.data.games.usecases.commons.PaginationMapper
import com.paulrybitskyi.gamedge.data.games.usecases.commons.mapToDomainGames
import com.paulrybitskyi.gamedge.domain.games.usecases.SearchGamesUseCase
import com.paulrybitskyi.gamedge.domain.games.usecases.SearchGamesUseCase.Params
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@BindType
internal class SearchGamesUseCaseImpl @Inject constructor(
    private val gamesDataStores: GamesDataStores,
    private val dispatcherProvider: DispatcherProvider,
    private val networkStateProvider: NetworkStateProvider,
    private val gameMapper: GameMapper,
    private val paginationMapper: PaginationMapper,
    private val errorMapper: ErrorMapper
) : SearchGamesUseCase {


    override suspend fun execute(params: Params) = withContext(dispatcherProvider.io) {
        val searchQuery = params.searchQuery
        val pagination = paginationMapper.mapToDataPagination(params.pagination)

        if(networkStateProvider.isNetworkAvailable) {
            gamesDataStores.remote
                .searchGames(searchQuery, pagination)
                .onSuccess(gamesDataStores.local::saveGames)
                .mapEither(gameMapper::mapToDomainGames, errorMapper::mapToDomainError)
        } else {
            gamesDataStores.local
                .searchGames(searchQuery, pagination)
                .let(gameMapper::mapToDomainGames)
                .asSuccess()
        }
    }


}