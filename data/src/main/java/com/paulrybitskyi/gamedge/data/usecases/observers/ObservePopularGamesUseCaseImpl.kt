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

package com.paulrybitskyi.gamedge.data.usecases.observers

import com.paulrybitskyi.gamedge.core.providers.DispatcherProvider
import com.paulrybitskyi.gamedge.data.datastores.GamesLocalDataStore
import com.paulrybitskyi.gamedge.data.usecases.mapper.EntityMapper
import com.paulrybitskyi.gamedge.data.usecases.mapper.mapToDomainGames
import com.paulrybitskyi.gamedge.domain.usecases.observers.ObservePopularGamesUseCase
import com.paulrybitskyi.gamedge.domain.usecases.observers.ObservePopularGamesUseCase.Params
import com.paulrybitskyi.gamedge.domain.usecases.refreshers.RefreshPopularGamesUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class ObservePopularGamesUseCaseImpl(
    private val refreshGamesUseCase: RefreshPopularGamesUseCase,
    private val gamesLocalDataStore: GamesLocalDataStore,
    private val dispatcherProvider: DispatcherProvider,
    private val entityMapper: EntityMapper
) : ObservePopularGamesUseCase {


    override suspend fun execute(params: Params) = withContext(dispatcherProvider.io) {
        val pagination = params.pagination

        if(params.refresh) {
            refreshGamesUseCase.execute(RefreshPopularGamesUseCase.Params(pagination))
        }

        gamesLocalDataStore
            .observePopularGames(pagination.offset, pagination.limit)
            .map(entityMapper::mapToDomainGames)
    }


}